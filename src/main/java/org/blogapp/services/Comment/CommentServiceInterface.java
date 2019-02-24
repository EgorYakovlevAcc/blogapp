package org.blogapp.services.Comment;

import org.blogapp.model.Comment;
import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommentServiceInterface {
    void save(Comment comment);
    void deleteCommentByComm_id(Integer id);

    List<Comment> findCommentsByPost(Post post, Sort sort);
    List<Comment> findCommentsByAuthor (User author);
    Comment findCommentByCommId (int commId);
    List<Comment> findCommentsByPostAndLevel (Post post, int level, Sort sort);
}
