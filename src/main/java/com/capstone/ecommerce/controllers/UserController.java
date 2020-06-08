package com.capstone.ecommerce.controllers;

import com.capstone.ecommerce.model.User;
import com.capstone.ecommerce.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

    //Tells controller where to get info from
@Controller
public class UserController {
        private UserRepository users;
        private PasswordEncoder passwordEncoder;


        //Stores info in variable so it can be used elsewhere, allows information to be malleable
        public UserController(UserRepository users, PasswordEncoder passwordEncoder) {
            this.users = users;
            this.passwordEncoder = passwordEncoder;
        }

        @GetMapping("/register")
        public String showSignupForm(Model model) {
            model.addAttribute("user", new User());
            return "users/register";
        }


    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAdmin(false);
        users.save(user);
        return "redirect:/login";
    }

//        @PostMapping("/register")
//        public String saveUser(@ModelAttribute User user) {
//            String hash = passwordEncoder.encode(user.getPassword());
//            user.setPassword(hash);
//            user.setAdmin(false);
//            users.save(user);
//            return "redirect:/login";
//        }

        @GetMapping("/users/profile")
        public String viewProfile(Model model) {
            // Get Current user and store to model to be sent to HTML
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            return "users/profile";
        }
    }

