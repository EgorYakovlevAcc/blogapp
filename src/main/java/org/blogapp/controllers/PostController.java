package org.blogapp.controllers;

import org.blogapp.model.Post;
import org.blogapp.repository.PostRepository;
import org.blogapp.services.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

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

    @GetMapping(value = "/all_posts")
    public String showAllPosts (Model model, String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("posts", postService.findPostsByTopicIsNotNull());
        return "all_posts";
    }


}
