package com.authentication.repository;

import com.authentication.dto.CommonUserDetails;
import com.authentication.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    UserDetails findByUserId(String userId);

    UserDetails findByUserName(String username);

    boolean existsByUserId(String userId);

    boolean existsByEmailId(String emailId);

    boolean existsByUserName(String userName);
}
