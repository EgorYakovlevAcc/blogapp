package org.blogapp.repository;

import org.blogapp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findPostById (Integer id);
    List<Post> findPostsByTopicIsNotNull ();
    List<Post> findPostsByAuthor(String author);
}