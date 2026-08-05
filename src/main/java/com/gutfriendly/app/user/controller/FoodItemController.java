package com.gutfriendly.app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.FoodItemDTO;
import com.gutfriendly.app.user.service.FoodItemService;

@RestController
@RequestMapping("/foods")
public class FoodItemController {

    @Autowired
    private FoodItemService foodService;

    // Get available menu items of a particular shop
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<Page<FoodItemDTO>> getMenuByShop(
            @PathVariable int shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "foodName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FoodItemDTO> menu =
                foodService.getMenuByShop(shopId, pageable);

        return ResponseEntity.ok(menu);
    }

    // Get details of one food item
    @GetMapping("/{foodId}")
    public ResponseEntity<FoodItemDTO> getFoodById(
            @PathVariable int foodId) {

        FoodItemDTO food =
                foodService.getFoodById(foodId);

        return ResponseEntity.ok(food);
    }

    // Search available food items by name
    @GetMapping("/search")
    public ResponseEntity<Page<FoodItemDTO>> searchFood(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "foodName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FoodItemDTO> foodItems =
                foodService.searchFood(keyword, pageable);

        return ResponseEntity.ok(foodItems);
    }
}