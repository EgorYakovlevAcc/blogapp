package org.blogapp.services.Comment;

import lombok.NoArgsConstructor;
import org.blogapp.model.Comment;
import org.blogapp.model.Post;
import org.blogapp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class CommentService implements CommentServiceInterface {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findCommentsByOfPost(post);
    }
}
