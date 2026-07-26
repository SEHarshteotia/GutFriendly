package com.gutfriendly.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.model.MenuItem;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {
}
