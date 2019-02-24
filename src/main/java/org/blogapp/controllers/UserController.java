package org.blogapp.controllers;

import org.blogapp.model.*;
import org.blogapp.services.Email.EmailService;
import org.blogapp.services.Post.PostService;
import org.blogapp.services.Subscriber.SubscriberService;
import org.blogapp.services.User.CustomSecurityService;
import org.blogapp.services.User.CustomUserService;
import org.blogapp.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.soap.SOAPBinding;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private CustomUserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SubscriberService subscriberService;

    @GetMapping(value = "/about")
    public String aboutPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "about";
    }

    @GetMapping(value = {"/", "/index"})
    public String welcome(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("name", loggedInUser.getName());
        if (loggedInUser.getName() != "anonymousUser") {
            model.addAttribute("isAuthed", true);
        } else {
            model.addAttribute("isAuthed", false);
        }

        return "index";
    }

    @GetMapping(value = {"/user_page/{id}"})
    public String user_page(@PathVariable("id") int id, Model model, @AuthenticationPrincipal User user) {
        boolean notSame = !user.equals(userService.findUserById(id));
        model.addAttribute("notSame", notSame);
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("posts", postService.findPostsByAuthor(userService.findUserById(id)));
        model.addAttribute("subs", userService.findUserById(id).getSubscription());
        model.addAttribute("followers", userService.findUserById(id).getFollowers());
        return "user_page";
    }

    @GetMapping(value = {"index/{email}"})
    public ResponseEntity<Map> Confirmation(Model model, HttpServletRequest request, @AuthenticationPrincipal User user, @PathVariable("email") String email) throws MessagingException {
        Map map = new HashMap<>();
        if (subscriberService.findSubscriberByEmail(email) == null) {
            Subscriber subscriber = new Subscriber();
            subscriber.setEmail(email);
            subscriber.setDate(new Date());
            subscriberService.save(subscriber);

            String appUrl = request.getScheme() + "://" + request.getServerName() + ":8080";
            Mail mail = new Mail();

            mail.setFrom("ecrire.moscow@gmail.com");
            mail.setTo(user.getEmail());
            mail.setSubject("confirm registration");

            String htmlContent = "<h3 style = 'text-align: center'>Hello, " + user.getUsername() + "</h3> <br> " +
                    "<div style='text-align: center'>" +
                    "<p> you have subscribed to news from our site </p></div>";
            mail.setContent(htmlContent);

            emailService.sendSimpleMessage(mail);
            map.put("answ", "success");
        } else {
            map.put("answ", "you have subscribed to us");
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping(value = {"/user_page"})
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes, @AuthenticationPrincipal User user) {
        attributes.addAttribute("id", userService.findByUsername(user.getUsername()).getId());
        return new RedirectView("/user_page/{id}");
    }

    @PostMapping(value = {"/user_page/{id}/follow"})
    public String follow(Model model, @AuthenticationPrincipal User user, @PathVariable("id") int id) {
        if (!userService.findUserById(id).equals(userService.findByUsername(user.getUsername()))) {
            if (!userService.findUserById(id).getFollowers().contains(userService.findByUsername(user.getUsername()))) {
                userService.findUserById(id).getFollowers().add(userService.findByUsername(user.getUsername()));
            } else {
                userService.findUserById(id).getFollowers().remove(userService.findByUsername(user.getUsername()));
            }
            userService.save(user);
        }
        return "redirect:/user_page/{id}";

    }

}
