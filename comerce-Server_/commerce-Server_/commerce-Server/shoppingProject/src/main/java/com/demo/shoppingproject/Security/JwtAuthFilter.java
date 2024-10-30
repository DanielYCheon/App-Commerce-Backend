package com.demo.shoppingproject.Security;
import com.demo.shoppingproject.Config.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Enumeration;
/*
HTTP
    Header:{
        Authorization: Bearer {jwt token}
            }
 */

/* About this Filter:
 * 1. All request and response cannot be null
 * 2. Check JWT Token Validation
 *
 * */
@Component
@RequiredArgsConstructor //Generates a constructor with required fields

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    //--------Checkin Error for Header is empty------//
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    String jwtToken = null;


    @Override
    protected void doFilterInternal
            (
                    //Every C.R.U.D request and response cannot be null
                    @NonNull HttpServletRequest request,
                    @NonNull HttpServletResponse response,
                    @NonNull FilterChain filterChain
            ) throws ServletException, IOException {   //Pass Token within the header  C++: const -> Java: final
        final String authHeader = request.getHeader("Authorization");
        //Bearer Token
     //   final String username;
        String username = null;
        String jwtToken = null;

        //---------------Checking the Header is Empty-----------------//
        logger.info("Authorization Header:" + authHeader);
/*
        if(request.getCookies() != null){
            for(Cookie cookie: request.getCookies()){
                if(cookie.getName().equals("AccessToken")){
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }
        */




        //Condition authHeader=null or authHeader does not start with "Bearer "

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Token is null or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;

        }



// Extract the JWT token from the authHeader
        jwtToken = authHeader.substring(7);

// Extract the username from the JWT token
        username = jwtUtil.extractUsername(jwtToken);

        //Print the header names and values for check
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
            }
        }


        //Check if the username is not null and the user is not authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //Check if the user is valid from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            //To check user valid or not
            //then, (1) Update the Security Context (2) Send Dispatchservlet
            if(jwtUtil.isTokenValid(jwtToken, userDetails)){

                //If user and token is valid, then create an Authentication Token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                //Set the details of the Authentication Token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //(1) Update the Authentication Token, which is update context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //Pass the request to the next filter
        filterChain.doFilter(request, response);
    }
}

