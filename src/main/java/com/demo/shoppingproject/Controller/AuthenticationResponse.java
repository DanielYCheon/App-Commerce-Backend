package com.demo.shoppingproject.Controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    // token will send back to the user
    private String accessToken;
    private String refreshToken;
    private String username;
    private String email;

}
