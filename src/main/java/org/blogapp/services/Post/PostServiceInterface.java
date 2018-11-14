package org.blogapp.services.Post;

import org.blogapp.model.Post;

import java.util.List;

public interface PostServiceInterface {
    void save(Post post);

    Post findPostById(Integer id);
    List<Post> findPostsByTopicIsNotNull();
    List<Post> findPostsByAuthor (String author);
}

