package org.blogapp.services.Post;

import org.blogapp.model.Post;
import org.blogapp.model.User;

import java.util.List;

public interface PostServiceInterface {
    void save(Post post);
    void deletePostById(Integer id);

    Post findPostById(Integer id);
    List<Post> findPostsByTopic(String topic);
    List<Post> findPostsByAuthor(User author);
    List<Post> findAll();
}

