package com.authentication.controller;


import com.authentication.dto.ApiResponse;
import com.authentication.dto.TokenResponse;
import com.authentication.model.UserDetails;
import com.authentication.service.JwtService;
import com.authentication.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.authentication.util.UserDetailsUtil.successObject;

@RestController
@RequestMapping("/v1/api/authentication")
public class AuthenticationController {

    private final JwtService jwtService;

    @Autowired
    private UserDetailsService service;

    public AuthenticationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDetails>> registerUser(@RequestBody UserDetails userDetails) {
        service.createUser(userDetails);
        return successObject("User created successfully");
    }

    @GetMapping("/accesstoken")
    public ResponseEntity<TokenResponse> getAccessToken(@RequestHeader("username") String username,
                                                        @RequestHeader("password") String password) {
        var response = jwtService.generateToken(username, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenResponse> validateAccessToken(@RequestHeader("Authorization") String token) {
        var response = jwtService.validateToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
