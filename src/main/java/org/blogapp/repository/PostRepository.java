package org.blogapp.repository;

import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findPostById (Integer id);
    List<Post> findPostsByTopic(String topic);
    List<Post> findPostsByAuthor(User author);
    void deletePostById (Integer id);

    @Override
    List<Post> findAll();
}
