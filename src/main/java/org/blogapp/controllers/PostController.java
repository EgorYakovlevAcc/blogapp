package org.blogapp.controllers;

import org.blogapp.model.Comment;
import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.blogapp.repository.PostRepository;
import org.blogapp.services.Comment.CommentService;
import org.blogapp.services.Post.PostService;
import org.blogapp.services.User.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CustomUserService userService;

    @GetMapping(value = "/create_post")
    public String createPost (Model model, String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @PostMapping(value = "/create_post")
    public String createPost (@ModelAttribute("post") Post post, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "create_post";
        }
        post.setAuthor(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        postService.save(post);
        return "redirect:/index";
    }

    @GetMapping(value = "/post/{id}")
    public String showAllPosts (Model model, @PathVariable("id") int id) {
        boolean isAuthor = SecurityContextHolder.getContext().getAuthentication()
                .getName().equals(postService.findPostById(id).getAuthor());
        System.out.println("isAuthor " + isAuthor);
        model.addAttribute("post", postService.findPostById(id));
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("comments", commentService
                .findCommentsByPost(postService.findPostById(id)));
        model.addAttribute("comment", new Comment());
        return "post";
    }

}
