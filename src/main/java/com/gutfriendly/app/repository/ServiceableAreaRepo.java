package com.gutfriendly.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.model.ServiceableArea;

public interface ServiceableAreaRepo extends JpaRepository<ServiceableArea, Integer> {

	boolean existsByPincode(String pincode);
}
