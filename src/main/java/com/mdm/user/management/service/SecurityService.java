package com.mdm.user.management.service;

import com.mdm.user.management.entities.AppUser;
import com.mdm.user.management.repositories.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service

public class SecurityService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManger;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private com.mdm.user.management.repositories.userRepo userRepo;

    @ExceptionHandler(Exception.class)
    public String verify(AppUser user) {
        SecurityContext holder = SecurityContextHolder.getContext();
        user = (AppUser) holder.getAuthentication().getPrincipal();
//        Authentication authCated = authManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        System.out.println("M1" +  holder.getAuthentication().isAuthenticated());
//        System.out.println("M1" + authCated.isAuthenticated());
        if (holder.getAuthentication().isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "failure";
        }
    }

        public AppUser registerAppUser(AppUser user){
            user.setPassword(encoder.encode(user.getPassword()));
            return userRepo.save(user);

        }
}
