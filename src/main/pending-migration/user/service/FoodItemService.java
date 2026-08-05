package com.gutfriendly.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.FoodItemDTO;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.FoodImages;
import com.gutfriendly.app.user.model.FoodItemsDetails;
import com.gutfriendly.app.user.repository.FoodItemsDetailsRepository;
import com.gutfriendly.app.user.repository.ShopDetailsRepository;

@Service
@Transactional(readOnly = true)
public class FoodItemService {

    @Autowired
    private FoodItemsDetailsRepository foodRepo;

    @Autowired
    private ShopDetailsRepository shopRepo;

    // Returns one page of available food items
    // belonging to a particular shop.
    public Page<FoodItemDTO> getMenuByShop(
            int shopId,
            Pageable pageable) {

        if (!shopRepo.existsById(shopId)) {
            throw new ResourceNotFoundException(
                    "Shop not found"
            );
        }

        Page<FoodItemsDetails> foodPage =
                foodRepo.findByShopShopIdAndAvailableTrue(
                        shopId,
                        pageable
                );

        return foodPage.map(this::convertToDTO);
    }

    // Returns the details of one food item.
    public FoodItemDTO getFoodById(int foodId) {

        FoodItemsDetails food = foodRepo.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food item not found"
                        ));

        return convertToDTO(food);
    }

    // Searches available food items by name
    // and returns paginated results.
    public Page<FoodItemDTO> searchFood(
            String keyword,
            Pageable pageable) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            throw new BadRequestException(
                    "Search keyword cannot be empty"
            );
        }

        return foodRepo
                .findByFoodNameContainingIgnoreCaseAndAvailableTrue(
                        keyword.trim(),
                        pageable
                )
                .map(this::convertToDTO);
    }

    // Converts FoodItemsDetails entity into FoodItemDTO.
    private FoodItemDTO convertToDTO(
            FoodItemsDetails food) {

        List<String> imageUrls =
                new ArrayList<>();

        if (food.getImages() != null) {

            for (FoodImages image : food.getImages()) {

                if (image != null &&
                        image.getImageUrl() != null) {

                    imageUrls.add(
                            image.getImageUrl()
                    );
                }
            }
        }

        return new FoodItemDTO(
                food.getFoodId(),
                food.getFoodName(),
                food.getPrice(),
                food.getFoodDesc(),
                food.getFoodCategory(),
                food.isAvailable(),
                imageUrls
        );
    }
}