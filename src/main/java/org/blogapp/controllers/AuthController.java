package org.blogapp.controllers;

import org.blogapp.model.Mail;
import org.blogapp.model.Role;
import org.blogapp.model.User;
import org.blogapp.services.Email.EmailService;
import org.blogapp.services.Post.PostService;
import org.blogapp.services.User.CustomSecurityService;
import org.blogapp.services.User.CustomUserDetailsService;
import org.blogapp.services.User.CustomUserService;
import org.blogapp.validators.UserValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
public class AuthController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private CustomUserService userService;
    @Autowired
    private CustomSecurityService securityService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping(value = "/registration")
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult, Model model, HttpServletRequest request) throws MessagingException {
        String password = user.getPassword();
        String username = user.getUsername();
        user.setRole(Role.USER);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(user);
        securityService.autologin(request, username, password);
        return "redirect:/index";
    }


    @GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("loginError", "Your username and password is invalid.");
        }
        if (logout != null)
            model.addAttribute("loginError", "You have been logged out successfully.");
        model.addAttribute("user", new User());

        return "login";
    }

    @PostMapping(value = "/login")
    public String login(Model model,
                        @ModelAttribute("user") User user) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);

        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        return "redirect:/user_page";
    }
}
