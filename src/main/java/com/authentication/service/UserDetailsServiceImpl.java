package com.authentication.service;

import com.authentication.dto.CommonUserDetails;
import com.authentication.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private CommonUserDetails commonUserDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.authentication.model.UserDetails userDetails = userDetailsRepository.findByUserName(username);
        commonUserDetails.setUserName(userDetails.getUserName());
        commonUserDetails.setPassword(userDetails.getPassword());
        commonUserDetails.setRoles(userDetails.getRoles());
        return new UserDetailsImpl(commonUserDetails);
    }
}
