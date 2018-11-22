package org.blogapp.controllers;

import org.blogapp.model.Post;
import org.blogapp.services.Post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.blogapp.validators.UserValidator;
import org.blogapp.model.User;
import org.blogapp.services.User.CustomSecurityService;
import org.blogapp.services.User.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private CustomUserService userService;
    @Autowired
    private CustomSecurityService securityService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PostService postService;

    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping(value = "/registration")
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
       userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(user);
        securityService.autologin(user.getUsername(), user.getPassword());
        return "redirect:/user_page";
    }

    @GetMapping(value = "/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("loginError", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("loginError", "You have been logged out successfully.");
        model.addAttribute("user", new User());

        return "login";
    }

    @PostMapping(value = "/login")
    public String login (Model model,
                         @ModelAttribute("user") User user) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        return "redirect:/user_page";
    }

    @GetMapping(value = {"/", "/index"})
    public String welcome(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("name", loggedInUser.getName());
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("listPost", postService.findAll());
        return "index";
    }

    @GetMapping(value = {"/user_page/{id}"})
    public String user_page(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("posts", postService.findPostsByAuthor(userService.findUserById(id)));
        return "user_page";
    }

    @GetMapping(value = {"/user_page"})
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes) {
        attributes.addAttribute("id", userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        return new RedirectView("/user_page/{id}");
    }

    @GetMapping(value = {"/", "/index"}, params = "search_posts")
    public String searchPosts (Model model, @RequestParam("search_posts") String search_posts) {
        System.out.println("Topic: " + search_posts);
        List<Post> listPost = postService.findPostsByTopic(search_posts);
        model.addAttribute("listPost", listPost);
        return "/index";
    }
}
