package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.UserPaymentStatus;

public interface UserPaymentStatusRepository extends JpaRepository<UserPaymentStatus, Integer> {

}
