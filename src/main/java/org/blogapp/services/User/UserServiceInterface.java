package org.blogapp.services.User;

import org.blogapp.model.User;

public interface UserServiceInterface {
    void save(User user);

    User findUserById(Integer id);
    User findByUsername(String username);
}
