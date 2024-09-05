package com.teste.selaz.security;

import com.teste.selaz.exception.EntityNotFoundException;
import com.teste.selaz.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = this.recoverToken(request);
            if (token != null) {
                var username = tokenService.validateToken(token);
                UserDetails user = userRepository.findByUsername(username);
                if (user == null) {
                    throw new EntityNotFoundException("User not found");
                }
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        filterChain.doFilter(request, response);
    }
        catch (EntityNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid credentials");
            response.getWriter().flush();
        }
    }

    private String recoverToken (HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null ) return null;
        return authHeader.replace("Bearer ", "");

    }
}