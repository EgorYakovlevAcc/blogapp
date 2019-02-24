package org.blogapp.controllers;

import org.blogapp.model.Comment;
import org.blogapp.model.User;
import org.blogapp.services.Comment.CommentService;
import org.blogapp.services.Post.PostService;
import org.blogapp.services.User.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.*;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private CustomUserService userService;

    @PostMapping("/add_comment/{id}")
    public ResponseEntity<Map> addComment(Model model, @ModelAttribute("comment") Comment comment, @PathVariable("id") int id) {
        comment.setPost(postService.findPostById(id));
        comment.setAuthor(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        comment.setLevel(0);
        comment.setDate(new Date());
        commentService.save(comment);

        Map map = new HashMap();
        map.put("text", comment.getMessage());
        map.put("author", comment.getAuthor().getUsername());
        map.put("date", comment.getDate());
        map.put("id", comment.getCommId());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/delete_comment/{Id}")
    public String addComment(Model model, @PathVariable("Id") int Id) {
        System.out.println("ededesedse");
            commentService.deleteCommentByComm_id(Id);
        return null;
    }

    @PostMapping("post/add_subcomment/{id}")
    public String addSubComment(Model model, @ModelAttribute Comment comment,
                                @PathVariable("id") int id, @AuthenticationPrincipal User user) {
        comment.setAuthor(userService.findByUsername(user.getUsername()));
        comment.setParent(commentService.findCommentByCommId(id));
        comment.setPost(comment.getParent().getPost());
        comment.setLevel(comment.getParent().getLevel() + 1);
        comment.setDate(new Date());
        int post_id = comment.getParent().getPost().getId();
        commentService.save(comment);

        List refreshSubcomments = new ArrayList<>();
        refreshSubcomments = commentService.findCommentByCommId(id).getSubcomments();
        refreshSubcomments.add(comment);
        commentService.findCommentByCommId(id).setSubcomments(refreshSubcomments);
        commentService.save(commentService.findCommentByCommId(id));

        return "redirect:/post/" + post_id;
    }
}
