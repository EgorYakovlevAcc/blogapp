package org.blogapp.services.User;

import lombok.NoArgsConstructor;
import org.blogapp.repository.UserRepository;
import org.blogapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

        @Override
        public User findUserById (Integer id) {
            return userRepository.findUserById(id);
        }
    }

