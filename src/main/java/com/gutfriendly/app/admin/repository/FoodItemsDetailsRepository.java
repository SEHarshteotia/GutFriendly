package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.FoodItemsDetails;

public interface FoodItemsDetailsRepository   extends JpaRepository<FoodItemsDetails, Integer> {

}
