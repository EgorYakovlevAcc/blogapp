package org.blogapp.services;

import lombok.NoArgsConstructor;
import org.blogapp.UserRepository;
import org.blogapp.model.User;
import org.blogapp.model.User.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        UserBuilder userBuilder = null;
        userBuilder.username(user.getPassword());
        userBuilder.password(user.getPassword());

        return (UserDetails) userBuilder.build();
    }
}
