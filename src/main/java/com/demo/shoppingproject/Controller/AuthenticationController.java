package com.demo.shoppingproject.Controller;

import com.demo.shoppingproject.Config.JwtUtil;
import com.demo.shoppingproject.Model.User;
import com.demo.shoppingproject.Repository.UserRepository;
import com.demo.shoppingproject.Service.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Authentication Controller provides 2 endpoints:
 *  1. Create a new user
 *  2. Authenticate existing user
 */

@RestController
@RequestMapping("/api/auth")
//Lombok's annotation that generates constructors for all final and non-null fields.
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService service;
private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    Optional<User> user = userRepository.findByUsername(request.getUsername());
    if (user.isPresent()) {
        return ResponseEntity.badRequest().body("User already exists");
    }
    return ResponseEntity.ok(service.register(request));
}


    //below code for User Login
@PostMapping("/authenticate")
public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    try {
        // Authenticate the user and get the response
        AuthenticationResponse authResponse = service.authenticate(request);

        // Create a HTTPonly cookie

        ResponseCookie refreshTokenCookie = ResponseCookie.from("RefreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(7*24*60*60) // 7day
                .path("/")
                .build();

        // Return the response with the Set-Cookie header
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(authResponse);
    } catch (UsernameNotFoundException error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("Invalid username or password", null, null,null));
    } catch (ExpiredJwtException error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("Token expired. Please refresh token.", null, null,null));
    }
}



    @PostMapping("/refresh")
   public ResponseEntity<?> refresh(@RequestBody AuthenticationResponse refreshRequest) {

       String refreshToken = refreshRequest.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            System.out.println("Refresh token is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token cannot be null or empty");
        }
        String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken);
        if (username == null || username.isEmpty()) {
            System.out.println("Username is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be null or empty");
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (jwtUtil.isRefreshTokenValid(refreshToken, user)) {
                String newJwtToken = jwtUtil.generateToken(user);
                return ResponseEntity.ok(Map.of("AccessToken", newJwtToken));
            } else {
                System.out.println("Invalid refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } else {
            System.out.println("User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

    }
    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken(@RequestBody Map<String, String> tokenBody) {
        String token = tokenBody.get("Access Token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token cannot be null or empty");
        }

        String username = jwtUtil.extractUsername(token);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean isTokenValid = jwtUtil.isTokenValid(token, user);
            System.out.println("Username from token: " + username);
            System.out.println("Username from database: " + user.getUsername());
            System.out.println("Token expiration date: " + jwtUtil.getExpirationDateFromToken(token));
            if (isTokenValid) {
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        if (request.getNewPassword().equals(request.getOldPassword())) {
            return ResponseEntity.badRequest().body("New password cannot be the same as the old password");
        }
        service.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }









}
