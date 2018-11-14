package org.blogapp.services.User;

public interface SecurityServiceInterface {
    String findLoggedInUsername();

    void autologin(String username, String password);

}
