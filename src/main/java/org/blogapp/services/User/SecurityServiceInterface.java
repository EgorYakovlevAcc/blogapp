package org.blogapp.services.User;

import javax.servlet.http.HttpServletRequest;

public interface SecurityServiceInterface {

    void autologin(HttpServletRequest request, String username, String password);

}
