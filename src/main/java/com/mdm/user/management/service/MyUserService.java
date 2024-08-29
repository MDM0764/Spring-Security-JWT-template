package com.mdm.user.management.service;

import com.mdm.user.management.entities.AppUser;
import com.mdm.user.management.repositories.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserService implements UserDetailsService {


    @Autowired
    private userRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManger;

    @Autowired
    private JwtService jwtService;

    public AppUser registerAppUser(AppUser user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> apUser= userRepo.findByEmail(username);
        return apUser.get();
    }

    public String verify(AppUser user) {
        Authentication authCated = authManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authCated.isAuthenticated()) {
            return jwtService.generateToken(user);
        } else {
            return "failure";
        }

    }
}
