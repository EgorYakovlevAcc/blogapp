package org.blogapp.services;

import org.blogapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

//public interface UserServiceInterface extends UserDetailsService {
//    User getUserById(Integer id);
//    void saveUser(User user);
//    void updateUser(Integer id, String username, String email);
//    void deleteUser(Integer id);
//    List<User> findAll();
//}

public interface UserServiceInterface {
    void save(User user);

    User findByUsername(String username);
}
