package org.blogapp.services.Comment;

import org.blogapp.model.Comment;
import org.blogapp.model.Post;

import java.util.List;

public class BypassInDepth {

    private Post post;
    private CommentService commentService;

    public BypassInDepth (Post post, CommentService commentService) {
        this.post = post;
        this.commentService = commentService;
    }

    public List<Comment> bypass (List<Comment> comments, List<Comment> reshape) {
        for (Comment c: comments) {
            reshape.add(c);
            if (c.getSubcomments() != null) {
                bypass(c.getSubcomments(), reshape);
            }
            else break;
        }
        return reshape;
    }
}
