package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

}
