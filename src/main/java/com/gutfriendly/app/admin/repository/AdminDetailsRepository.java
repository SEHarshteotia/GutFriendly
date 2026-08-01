package com.gutfriendly.app.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.AdminDetails;

public interface AdminDetailsRepository extends JpaRepository<AdminDetails, Integer> {
	 Optional<AdminDetails> findByEmail(String email);


	 boolean existsByEmail(String email);

}
