package com.demo.shoppingproject.Service;

import com.demo.shoppingproject.Config.JwtUtil;
import com.demo.shoppingproject.Controller.AuthenticationRequest;
import com.demo.shoppingproject.Controller.AuthenticationResponse;
import com.demo.shoppingproject.Controller.ChangePasswordRequest;
import com.demo.shoppingproject.Controller.RegisterRequest;
import com.demo.shoppingproject.Model.AddCart;
import com.demo.shoppingproject.Model.Role;
import com.demo.shoppingproject.Model.User;
import com.demo.shoppingproject.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final HttpServletResponse httpServletResponse;



    private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());


    //Register the user and save the user details in the database, then return the token
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();

        AddCart addCart = new AddCart();
        addCart.setUser(user);
        user.setAddCart(addCart);
        System.out.println(" Successfully create shopping cart");

        userRepository.save(user);
        //Access token
        String jwtToken = jwtUtil.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();

    }
    //Authenticate the user and return the token
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )

        );


        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .username(user.getUsername()) //return the username when response
                .email(user.getEmail())
                .build();
    }
    public void changePassword(ChangePasswordRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new BadCredentialsException("Old password is incorrect");

        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


}
