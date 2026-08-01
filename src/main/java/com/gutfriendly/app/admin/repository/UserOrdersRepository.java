package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.UserOrders;

public interface UserOrdersRepository extends JpaRepository<UserOrders, Integer> {

}
