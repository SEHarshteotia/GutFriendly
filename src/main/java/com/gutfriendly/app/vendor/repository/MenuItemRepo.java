package com.gutfriendly.app.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.MenuItem;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {
}
