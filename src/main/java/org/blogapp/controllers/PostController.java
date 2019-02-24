package org.blogapp.controllers;

import com.google.common.collect.Maps;
import org.blogapp.model.Comment;
import org.blogapp.model.Mail;
import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.blogapp.repository.PostRepository;
import org.blogapp.services.Comment.BypassInDepth;
import org.blogapp.services.Comment.CommentService;
import org.blogapp.services.Email.EmailService;
import org.blogapp.services.Post.PostService;
import org.blogapp.services.User.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CustomUserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/create_post")
    public String createPost(Model model, String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @GetMapping(value = "/edit_post/{id}")
    public String createPost(Model model, String error, @PathVariable("id") int id) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("post", postService.findPostById(id));
        return "/edit_post";
    }

    @PostMapping(value = "/edit_post/{id}")
    public String editPost(HttpServletRequest request, @PathVariable("id") int id, @ModelAttribute("post") Post post, BindingResult bindingResult, Model model, @AuthenticationPrincipal User user) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "edit_post";
        }
        postService.findPostById(id).setTitle(post.getTitle());
        postService.findPostById(id).setTopic(post.getTopic());
        postService.findPostById(id).setText(post.getText());
        postService.save(postService.findPostById(id));

        return"redirect:/index";
    }

    @PostMapping(value = "/create_post")
    public String createPost(HttpServletRequest request, @ModelAttribute("post") Post post, BindingResult bindingResult, Model model, @AuthenticationPrincipal User user) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "create_post";
        }
        post.setAuthor(userService.findByUsername(user.getUsername()));
        postService.save(post);

        Mail mail = new Mail();
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":8080";
        for (User u : userService.findAll()) {
            if (!u.equals(user)) {

                mail.setFrom("ecrire.moscow@gmail.com");
                mail.setSubject("new post we have");
                String htmlContent = "<h3 style = 'text-align: center'>Hello, " + u.getUsername() + "</h3> <br> " +
                        "<div style='text-align: center'>" +
                        "<p> User " + user.getUsername() + " have published new post " + post.getTitle() + " </p> <br>" +
                        "<p> you got this latter because you have subscribed to our blog</p> <br>" +
                        "<div style='text-align: right'>ecrire</div>" +
                        " </div>";
                mail.setContent(htmlContent);
                mail.setTo(u.getEmail());
                emailService.sendSimpleMessage(mail);
            }
        }

        return "redirect:/index";
    }

    @GetMapping(value = "/post/{id}")
    public String showAllPosts(Model model, @PathVariable("id") int id, @AuthenticationPrincipal User user) {
        if (!postService.findPostById(id).getViews().contains(user)) {
            postService.findPostById(id).getViews().add(user);
            postService.save(postService.findPostById(id));
        }
        boolean isAuthor = SecurityContextHolder.getContext().getAuthentication()
                .getName().equals(postService.findPostById(id).getAuthor().getUsername());
        model.addAttribute("post", postService.findPostById(id));
        System.out.println("Likes: " + postService.findPostById(id).getLikes().size());
        model.addAttribute("likes", postService.findPostById(id).getLikes().size());
        model.addAttribute("views", postService.findPostById(id).getViews().size());
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("user", user);
        model.addAttribute("isUserLiked", postService.findPostById(id).getLikes().contains(user));

        BypassInDepth bypassInDepth = new BypassInDepth(postService.findPostById(id), commentService);
        List<Comment> reshapeList = bypassInDepth.bypass(commentService.
                        findCommentsByPostAndLevel(postService.findPostById(id), 0, Sort.by("date")),
                new ArrayList<Comment>());
        model.addAttribute("comments", reshapeList);
        model.addAttribute("comment", new Comment());


        return "post";
    }


    @PostMapping(value = "/post/{id}/like")
    public ResponseEntity<Map> addLikeToPost(Model model, @PathVariable("id") int id, @AuthenticationPrincipal User user) {
        Post post = postService.findPostById(id);
        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
        }
        postService.save(post);
        Map map = new HashMap();
        map.put("value", post.getLikes().size());
        map.put("isLike", post.getLikes().contains(user));
        return ResponseEntity.ok(map);
    }

    @GetMapping(value = "/news")
    public String showNews(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("name", loggedInUser.getName());
        model.addAttribute("listPost", postService.findAll());
        model.addAttribute("listUser", userService.findAll());
        return "news";
    }

}
