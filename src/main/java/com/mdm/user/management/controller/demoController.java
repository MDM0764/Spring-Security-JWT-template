package com.mdm.user.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class demoController {

    @GetMapping("/access")
    public ResponseEntity<String> access() {
        return ResponseEntity.ok("code is working");
    }


}
