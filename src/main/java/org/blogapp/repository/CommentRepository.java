package org.blogapp.repository;

import org.blogapp.model.Comment;
import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findCommentsByPost(Post post, Sort sort);
    List<Comment> findCommentsByAuthor(User author);
    Comment findCommentByCommId (int commId);
    List<Comment> findCommentsByPostAndLevel (Post post, int level, Sort sort);

    void deleteCommentByCommId (Integer comm_id);
}
