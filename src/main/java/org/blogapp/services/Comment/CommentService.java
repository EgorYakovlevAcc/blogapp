package org.blogapp.services.Comment;

import lombok.NoArgsConstructor;
import org.blogapp.model.Comment;
import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.blogapp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public List<Comment> findCommentsByPost(Post post, Sort sort) {
        return commentRepository.findCommentsByPost(post, sort);
    }

    @Override
    public void deleteCommentByComm_id(Integer CommId) {
        commentRepository.deleteCommentByCommId(CommId);
    }

    @Override
    public List<Comment> findCommentsByAuthor(User author) {
        return commentRepository.findCommentsByAuthor(author);
    }

    @Override
    public Comment findCommentByCommId(int commId) {
        return commentRepository.findCommentByCommId(commId);
    }

    public List<Comment> findCommentsByPostAndLevel (Post post, int level, Sort sort) {
        return commentRepository.findCommentsByPostAndLevel(post, level, sort);
    }

//    public int calcLevel (Comment comment) {
//        int level = 0;
//
//        while (comment.getParent() != null) {
//            level++;
//            calcLevel(comment.getParent());
//        }
//        return level;
//    }
}
