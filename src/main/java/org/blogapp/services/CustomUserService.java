package org.blogapp.services;

import lombok.NoArgsConstructor;
import org.blogapp.UserRepository;
import org.blogapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.List;

//@Service
//@NoArgsConstructor
//public class CustomUserService implements UserServiceInterface {
    @Service
    @NoArgsConstructor
    public class CustomUserService implements UserServiceInterface {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
        public void save(User user) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }

        @Override
        public User findByUsername(String username) {
            return userRepository.findByUsername(username);
        }
    }
//    @Autowired
//    private UserRepository repository;
//
//
//    @Autowired
//    public void setRepository (UserRepository repository) {
//        this.repository = repository;
//    }
//    @Override
//    public User getUserById(Integer id) {
//        return repository.getOne(id);
//    }
//
//    @Override
//    public void saveUser(User user) {
//        repository.save(user);
//    }
//
//    @Override
//    public void updateUser(Integer id, String username, String email) {
//        User updated = repository.getOne(id);
//        updated.setUsername(username);
//        updated.setEmail(email);
//        repository.save(updated);
//    }
//
//    @Override
//    public void deleteUser(Integer id) {
//        repository.deleteById(id);
//    }
//
//    @Override
//    public List<User> findAll() {
//        return repository.findAll();
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
//}
