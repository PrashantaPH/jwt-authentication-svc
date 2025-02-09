package com.authentication.service;

import com.authentication.exception.UserAlreadyExistsException;
import com.authentication.exception.UserNotFoundException;
import com.authentication.model.UserDetails;
import com.authentication.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("customUserDetailsService")
public class UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository userRepository;

    public UserDetailsService(PasswordEncoder passwordEncoder, UserDetailsRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(UserDetails userDetails) {
        try {
            if (userRepository.existsByEmailId(userDetails.getEmailId()) || userRepository.existsByUserName(userDetails.getUserName())) {
                throw new UserAlreadyExistsException("User already exists!");
            }
            userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            userRepository.save(userDetails);
        } catch (UserAlreadyExistsException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Technical error accord");
        }
    }

    public UserDetails getUser(Integer id) {
        Optional<UserDetails> optional = userRepository.findById(id);
        if (optional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return optional.get();
    }

    public UserDetails getUser(String username) {
        UserDetails existingUser = userRepository.findByUserName(username);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }
        return existingUser;
    }
}
