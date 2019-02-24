package org.blogapp.services.User;

import org.blogapp.model.User;

import java.util.List;

public interface UserServiceInterface {

    void save(User user);
    void deleteUserById(Integer id);

    User findUserById(Integer id);
    User findByUsername(String username);
    List<User> findAll ();

}
