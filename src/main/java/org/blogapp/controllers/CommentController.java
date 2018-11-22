package org.blogapp.controllers;

import org.blogapp.model.Comment;
import org.blogapp.services.Comment.CommentService;
import org.blogapp.services.Post.PostService;
import org.blogapp.services.User.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private CustomUserService userService;

    @PostMapping(value = "/add_comment/{id}")
    public String addComment (Model model, @ModelAttribute("comment") Comment comment, @PathVariable("id") int id) {
        comment.setPost(postService.findPostById(id));
        comment.setAuthor(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        commentService.save(comment);
        return "redirect:/post/{id}";
    }
}
