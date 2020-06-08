package com.capstone.ecommerce.controllers;


import com.capstone.ecommerce.model.Address;
import com.capstone.ecommerce.model.ShoppingCart;
import com.capstone.ecommerce.model.User;
import com.capstone.ecommerce.repositories.AddressRepository;
import com.capstone.ecommerce.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Tells controller where to get info from

//Tells controller where to get info from

@Controller
@SessionAttributes("products")
public class UserController {
    private UserRepository users;
        private PasswordEncoder passwordEncoder;
    private AddressRepository addressRepository;

        //Stores info in variable so it can be used elsewhere, allows information to be malleable





    //Stores info in variable so it can be used elsewhere, allows information to be malleable

    public UserController(UserRepository users, PasswordEncoder passwordEncoder, AddressRepository addyRepo) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addyRepo;
    }

    @GetMapping("/register")
    public String showSignupForm(Model model){
        if(model.getAttribute("products") == null) {
            ShoppingCart products = new ShoppingCart();
            model.addAttribute("products", products);
        }
        model.addAttribute("user", new User());
        return "users/register";
    }


    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setAdmin(false);
        users.save(user);
        return "redirect:/login";
    }


//        @GetMapping("/users/profile/{name}")
//        public String findByUsername(@PathVariable String name, Model model) {
//            model.addAttribute("name", name);
//
//
//            return "/users/profile";
//        }

        @GetMapping("/users/profile")
        public String basicProfile(Model model) {
            User person = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = this.users.getOne(person.getId());
            model.addAttribute("user", user);
            List<Address> addresses = addressRepository.findAll();
            model.addAttribute("addresses", addresses);
            return "users/profile";
        }



//        @PostMapping("/register")
//        public String saveUser(@ModelAttribute User user) {
//            String hash = passwordEncoder.encode(user.getPassword());
//            user.setPassword(hash);
//            user.setAdmin(false);
//            users.save(user);
//            return "redirect:/login";
//        }
//
//        @GetMapping("/users/profile")
//        public String viewProfile(Model model) {
//            // Get Current user and store to model to be sent to HTML
//            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            model.addAttribute("user", user);
//            return "users/profile";
//        }
//
//    }
//    }

//    @GetMapping("/users/profile")
//    public String viewProfile(Model model) {
//        // Get Current user and store to model to be sent to HTML
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", user);
//        List<Address> addresses = addressRepository.findAll();
//        model.addAttribute("addresses", addresses);
//        return "users/profile";
//    }



    @GetMapping("users/addresses/add")
    public String showAddAddress() {
        return "users/addresses";
    }

    @PostMapping("users/addresses/add")
    public String addAddressPost(@RequestParam(name = "first_name") String firstName,
                                 @RequestParam(name = "last_name") String lastName,
                                 @RequestParam(name = "address_type") String type,
                                 @RequestParam(name = "street_1") String street1,
                                 @RequestParam(name = "street_2") String street2,
                                 @RequestParam(name = "city") String city,
                                 @RequestParam(name = "zipcode") int zipcode,
                                 @RequestParam(name = "state") String state) {
        Address address = new Address();
        address.setAddresstype(type);
        address.setFirstname(firstName);
        address.setLastname(lastName);
        address.setStreet1(street1);
        address.setStreet2(street2);
        address.setCity(city);
        address.setState(state);
        address.setZipcode(zipcode);
        addressRepository.save(address);
        return "users/profile";
    }

    @GetMapping("/users/addresses/edit/{id}")
    public String getDeleteProfileForm(@PathVariable long id, Model model){
        return "users/edit";
    }

    @PostMapping("/users/addresses/edit/")
    public String editProfile(@ModelAttribute Address address,
                              @RequestParam(name = "first_name") String firstName,
                              @RequestParam(name = "last_name") String lastName,
                              @RequestParam(name = "address_type") String type,
                              @RequestParam(name = "street_1") String street1,
                              @RequestParam(name = "street_2") String street2,
                              @RequestParam(name = "city") String city,
                              @RequestParam(name = "zipcode") int zipcode,
                              @RequestParam(name = "state") String state)

    {
        address.setAddresstype(type);
        address.setFirstname(firstName);
        address.setLastname(lastName);
        address.setStreet1(street1);
        address.setStreet2(street2);
        address.setCity(city);
        address.setState(state);
        address.setZipcode(zipcode);
        addressRepository.save(address);
        return "redirect:/users/profile";
    }

//
//    @PostMapping("users/addresses/edit")
//    public String editAddressPost(
//            @PathVariable long id,
//            @RequestParam(name = "first_name") String firstName,
//            @RequestParam(name = "last_name") String lastName,
//            @RequestParam(name = "address_type") String type,
//            @RequestParam(name = "street_1") String street1,
//            @RequestParam(name = "street_2") String street2,
//            @RequestParam(name = "city") String city,
//            @RequestParam(name = "zipcode") int zipcode,
//            @RequestParam(name = "state") String state) {
//        Address address = addressRepository.getOne(id);
//        address.setAddresstype(type);
//        address.setFirstname(firstName);
//        address.setLastname(lastName);
//        address.setStreet1(street1);
//        address.setStreet2(street2);
//        address.setCity(city);
//        address.setState(state);
//        address.setZipcode(zipcode);
//        addressRepository.save(address);
//        return "redirect:/users/profile";
//    }


}