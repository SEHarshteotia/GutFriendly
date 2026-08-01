package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

}
