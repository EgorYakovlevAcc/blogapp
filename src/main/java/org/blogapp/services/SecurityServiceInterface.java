package org.blogapp.services;

import org.springframework.security.authentication.AuthenticationManager;

public interface SecurityServiceInterface {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
