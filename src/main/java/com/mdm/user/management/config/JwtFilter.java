package com.mdm.user.management.config;

import com.mdm.user.management.service.JwtService;
import com.mdm.user.management.service.MyUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

   @Autowired
   private JwtService jwtService;

   @Autowired
   private ApplicationContext appContext;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHead =request.getHeader("Authorization");
        String token = null;
        String userName = null;
        if (authHead != null && authHead.startsWith("Bearer ")) {
            token = authHead.substring(7);
            userName = jwtService.extractUserName(token);
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDtls = appContext.getBean(MyUserService.class).loadUserByUsername(userName);

            if (jwtService.validateToken(token, userDtls)) {
                UsernamePasswordAuthenticationToken userPassAuthtoken = new UsernamePasswordAuthenticationToken(userDtls, null, userDtls.getAuthorities());
                userPassAuthtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userPassAuthtoken);
            }

        }
//        while (request.getAttributeNames().nextElement() != null) {
//            String e = (String) request.getAttributeNames().nextElement();
//            System.out.println("param name : " + e + " and param value : " + request.getAttribute(e));
//        }
        filterChain.doFilter(request, response);
    }
}
