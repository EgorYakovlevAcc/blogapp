package org.blogapp.controllers;

import org.blogapp.model.Post;
import org.blogapp.repository.PostRepository;
import org.blogapp.services.Comment.CommentService;
import org.blogapp.services.Post.PostService;
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

    @GetMapping(value = "/create_post")
    public String createPost (Model model, String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("post", new Post());
        model.addAttribute("author_name", SecurityContextHolder.getContext().getAuthentication().getName());
        return "create_post";
    }

    @PostMapping(value = "/create_post")
    public String createPost (@ModelAttribute("post") Post post, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "create_post";
        }
        System.out.println(post.getName());
        postService.save(post);
        return "index";
    }

    @GetMapping(value = "/post/id", params = "id")
    public String showAllPosts (Model model, @RequestParam Integer id) {
        model.addAttribute("post", postService.findPostById(id));
        model.addAttribute("comments", commentService
                .findCommentsByPost(postService.findPostById(id)));
        return "post";
    }

}
