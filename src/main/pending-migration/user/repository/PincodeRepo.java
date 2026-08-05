package com.gutfriendly.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gutfriendly.app.user.model.Pincode;
@Repository
public interface PincodeRepo extends JpaRepository<Pincode, String> {

}
