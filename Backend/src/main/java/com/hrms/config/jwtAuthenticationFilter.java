package com.hrms.config;


import com.hrms.acl.auth.springUser.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class jwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private JwtUtils jwtUtils;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("Token Header : "+requestTokenHeader);
        String username=null;
        String jwtToken=null;

        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")){

            //yes valid
            jwtToken=requestTokenHeader.substring(7);
            try {
                username = this.jwtUtils.extractUsername(jwtToken);
            }catch (ExpiredJwtException e){
                System.out.println("SETTING "+e.getMessage());

                request.setAttribute("Inside JwtAuthenticationFilter class ::expired", e.getMessage());
                // e.printStackTrace();
                System.out.println("jwt token has expired");
                throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "jwt token has expired");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error");
            }

        }else{
            System.out.println("Inside JwtAuthenticationFilter class :: Invalid token, not start with bearer string");
        }

        //validated
        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            final UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
            if (this.jwtUtils.validateToken(jwtToken,userDetails)){
                //token is valid
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }else {
            System.out.println("jwt token has expired");
        }

        filterChain.doFilter(request,response);

    }


}
