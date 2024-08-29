package com.mdm.user.management.controller;

import com.mdm.user.management.repositories.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/admin")
public class AdminController {




        @Autowired
        private userRepo repo;

        @GetMapping("/access")
        public String access() {
            return "admin";
        }
}
