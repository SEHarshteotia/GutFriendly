package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.Pincode;

public interface PincodeRepository  extends JpaRepository<Pincode, String>{

}
