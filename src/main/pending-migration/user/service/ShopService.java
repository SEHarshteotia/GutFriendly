package com.gutfriendly.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.ShopCardDTO;
import com.gutfriendly.app.user.dto.ShopDetailsDTO;
import com.gutfriendly.app.user.enums.Category;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.ShopDetails;
import com.gutfriendly.app.user.model.ShopImages;
import com.gutfriendly.app.user.repository.ShopDetailsRepository;

@Service
@Transactional(readOnly = true)
public class ShopService {

    @Autowired
    private ShopDetailsRepository shopRepo;

    // Returns all shops as shop cards.
    public List<ShopCardDTO> getAllShops() {

        return shopRepo.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Returns shops ordered by highest final GutTrust score.
    public List<ShopCardDTO> getTrustedVendors() {

        return shopRepo
                .findByOrderByFinalGutTrustScoreDesc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Searches shops by shop name.
    public List<ShopCardDTO> searchShops(
            String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            throw new BadRequestException(
                    "Search keyword cannot be empty"
            );
        }

        return shopRepo
                .findByShopNameContainingIgnoreCase(
                        keyword.trim()
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Returns shops belonging to one category.
    public List<ShopCardDTO> getShopsByCategory(
            Category category) {

        if (category == null) {
            throw new BadRequestException(
                    "Shop category is required"
            );
        }

        return shopRepo.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Returns complete details of one shop.
    public ShopDetailsDTO getById(int id) {

        ShopDetails shop = shopRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shop not found"
                        ));

        return convertToDetailsDTO(shop);
    }

    // Converts ShopDetails entity into a shop-card DTO.
    private ShopCardDTO convertToDTO(
            ShopDetails shop) {

        String locality = null;
        String imageUrl = null;
        String category = null;

        if (shop.getAddress_id() != null) {
            locality =
                    shop.getAddress_id().getLocality();
        }

        if (shop.getImages() != null &&
                !shop.getImages().isEmpty()) {

            for (ShopImages image : shop.getImages()) {

                if (image != null &&
                        image.getImageUrl() != null) {

                    imageUrl = image.getImageUrl();
                    break;
                }
            }
        }

        if (shop.getCategory() != null) {
            category =
                    shop.getCategory().name();
        }

        return new ShopCardDTO(
                shop.getShopId(),
                shop.getShopName(),
                category,
                locality,
                shop.getFinalGutTrustScore(),
                imageUrl
        );
    }

    // Converts ShopDetails entity into detailed response DTO.
    private ShopDetailsDTO convertToDetailsDTO(
            ShopDetails shop) {

        String locality = null;
        String pincode = null;
        String category = null;

        if (shop.getAddress_id() != null) {

            locality =
                    shop.getAddress_id().getLocality();

            if (shop.getAddress_id()
                    .getPin_code() != null) {

                pincode = shop.getAddress_id()
                        .getPin_code()
                        .getPin_code();
            }
        }

        if (shop.getCategory() != null) {
            category =
                    shop.getCategory().name();
        }

        List<String> imageUrls =
                new ArrayList<>();

        if (shop.getImages() != null) {

            for (ShopImages image : shop.getImages()) {

                if (image != null &&
                        image.getImageUrl() != null) {

                    imageUrls.add(
                            image.getImageUrl()
                    );
                }
            }
        }

        return new ShopDetailsDTO(
                shop.getShopId(),
                shop.getShopName(),
                category,
                shop.getUserTrustScore(),
                shop.getInspectionTrustScore(),
                shop.getFinalGutTrustScore(),
                locality,
                pincode,
                imageUrls
        );
    }
}