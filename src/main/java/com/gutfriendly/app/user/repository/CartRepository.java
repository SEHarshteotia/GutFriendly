package com.gutfriendly.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.user.model.Cart;
import com.gutfriendly.app.user.model.UserDetails;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(UserDetails user);
}