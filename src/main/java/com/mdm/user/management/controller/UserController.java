package com.mdm.user.management.controller;

import com.mdm.user.management.entities.AppUser;
import com.mdm.user.management.repositories.userRepo;
import com.mdm.user.management.service.MyUserService;
import com.mdm.user.management.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private userRepo repo;

    @Autowired
    private SecurityService service;

    @GetMapping("/access")
    public String access() {
        return "user";
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user){
        return service.registerAppUser(user);
    }

    @GetMapping("/login")
    public String login(@RequestBody AppUser user) {

       return service.verify(user);
    }

}
