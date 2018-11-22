package org.blogapp.services.Comment;

import org.blogapp.model.Comment;
import org.blogapp.model.Post;
import org.blogapp.model.User;

import java.util.List;

public interface CommentServiceInterface {
    void save(Comment comment);

    List<Comment> findCommentsByPost(Post post);
    List<Comment> findCommentsByAuthor (User author);
}
