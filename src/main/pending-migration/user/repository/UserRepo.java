package com.gutfriendly.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gutfriendly.app.user.model.UserDetails;

@Repository
public interface UserRepo extends JpaRepository<UserDetails, Integer> {

    UserDetails findByPhoneNo(String PhoneNo);

    
    
}
