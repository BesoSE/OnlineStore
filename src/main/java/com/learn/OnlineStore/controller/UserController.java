package com.learn.OnlineStore.controller;

import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.service.ProductService;
import com.learn.OnlineStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

     @Autowired
     private UserService userService;

     @Autowired
     private ProductService productService;

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") Long id, Model model){
        try{
            User user=userService.findById(id);
            model.addAttribute("user",user);
            model.addAttribute("products",productService.findAllByAuthor(user));
        }catch (RuntimeException e){
            model.addAttribute("message",e.getMessage());
        }

        return "userPage";
    }

    @GetMapping("/users/{id}")
    public String showUsers(@PathVariable("id") Long id, Model model){
        try{
            User user=userService.findById(id);
            model.addAttribute("user",user);
            model.addAttribute("products",productService.findAllByAuthor(user));
        }catch (RuntimeException e){
            model.addAttribute("message",e.getMessage());
        }

        return "getp";
    }
}
