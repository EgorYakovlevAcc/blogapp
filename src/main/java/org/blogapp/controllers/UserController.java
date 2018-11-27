package org.blogapp.controllers;

import org.blogapp.model.Post;
import org.blogapp.model.Role;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
       user.setRole(Role.USER);
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
    public String user_page(@PathVariable("id") int id, Model model, @AuthenticationPrincipal User user) {
        boolean isSame = !user.equals(userService.findUserById(id));
        model.addAttribute("isSame", isSame);
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("posts", postService.findPostsByAuthor(userService.findUserById(id)));
        model.addAttribute("subs", userService.findUserById(id).getSubscription());
        model.addAttribute("followers", userService.findUserById(id).getFollowers());
        System.out.println("THE SIZE OF ARRAY: " + userService.findUserById(id).getFollowers().size());
        return "user_page";
    }

    @GetMapping(value = {"/user_page"})
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes, @AuthenticationPrincipal User user) {
        attributes.addAttribute("id", userService.findByUsername(user.getUsername()).getId());
        return new RedirectView("/user_page/{id}");
    }

    @GetMapping(value = {"/", "/index"}, params = "search_posts")
    public String searchPosts (Model model, @RequestParam("search_posts") String search_posts) {
        List<Post> listPost = postService.findPostsByTopic(search_posts);
        model.addAttribute("listPost", listPost);
        return "/index";
    }

    @PostMapping(value = {"/user_page/{id}/follow"})
    public String follow (Model model, @AuthenticationPrincipal User user, @PathVariable("id") int id) {
//        User followedUser = userService.findUserById(id);
//        if (!user.getFollowers().contains(followedUser)) {
//            user.getFollowers().add(followedUser);
//        }
//        else {
//            System.out.println("I delete you");
//            user.getFollowers().remove(followedUser);
//        }
        if (!userService.findByUsername(user.getUsername()).getFollowers().contains(userService.findUserById(id))) {
            userService.findByUsername(user.getUsername()).getFollowers().add(userService.findUserById(id));
            System.out.println("INTO IF: ");
        }
        else {
            userService.findByUsername(user.getUsername()).getFollowers().remove(userService.findUserById(id));
            System.out.println("INTO ELSE: ");
        }
        userService.save(user);
        return "redirect:/user_page/{id}";
    }
}
