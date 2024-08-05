package com.example.eventmanager.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authHeader: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        if (!jwtTokenManager.validateToken(jwt)) {
            filterChain.doFilter(request, response);
            log.info("Jwt not valid");
            return;
        }

        String login = jwtTokenManager.getLoginFromToken(jwt);
        String role = jwtTokenManager.getRoleFromToken(jwt);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                login,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );

        log.info("Jwt authenticated");
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }

}
