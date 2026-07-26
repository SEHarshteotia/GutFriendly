package com.gutfriendly.app.vendor.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.ServiceableArea;

public interface ServiceableAreaRepo extends JpaRepository<ServiceableArea, Integer> {

	boolean existsByPincode(String pincode);
}
