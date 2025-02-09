package com.authentication.controller;

import com.authentication.dto.ApiResponse;
import com.authentication.model.UserDetails;
import com.authentication.service.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.authentication.util.UserDetailsUtil.successObject;

@RestController
@RequestMapping("v1/api/users")
@Validated
public class UserDetailsController {

    private final UserDetailsService userService;

    public UserDetailsController(UserDetailsService userService) {
        this.userService = userService;
    }

//    @Secured
    @PostMapping
    public ResponseEntity<ApiResponse<UserDetails>> create(@RequestHeader("Authorization") String authToken,
                                                           @RequestBody @Valid UserDetails userDetails) {
        userService.createUser(userDetails);
        return successObject("User created successfully!");
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<ApiResponse<UserDetails>> getUser(@PathVariable("username") String username) {
//        UserDetails user = userService.getUser(username);
//        return successObject("User fetched successfully!", user);
//    }
}
