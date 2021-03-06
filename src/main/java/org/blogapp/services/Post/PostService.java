package org.blogapp.services.Post;

import lombok.NoArgsConstructor;
import org.blogapp.model.Post;
import org.blogapp.model.User;
import org.blogapp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class PostService implements PostServiceInterface {
    @Autowired
    private PostRepository repository;
    @Override
    public void save(Post post) {
        repository.save(post);
    }

    @Override
    public Post findPostById(Integer id) {
        return repository.findPostById(id);
    }

    @Override
    public List<Post> findPostsByTopic(String topic) {
        return repository.findPostsByTopic(topic);
    }

    @Override
    public List<Post> findPostsByAuthor(User author) {
        return repository.findPostsByAuthor(author);
    }

    @Override
    public void deletePostById(Integer id) {
        repository.deletePostById(id);
    }

    @Override
    public List<Post> findAll() {
        return repository.findAll();
    }
}
