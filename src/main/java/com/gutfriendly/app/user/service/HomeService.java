package com.gutfriendly.app.user.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.HomePageDTO;
import com.gutfriendly.app.user.dto.ShopCardDTO;
import com.gutfriendly.app.admin.enums.Category;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.repository.UserRepo;

@Service
public class HomeService {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserRepo userRepo;

    // Returns all data required by the user's homepage.
    @Transactional(readOnly = true)
    public HomePageDTO getHomePage(int userId) {

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        HomePageDTO home = new HomePageDTO();

        List<ShopCardDTO> allShops =
                shopService.getAllShops();

        List<ShopCardDTO> trustedVendors =
                shopService.getTrustedVendors();

        home.setAllShops(allShops);
        home.setTrustedVendors(trustedVendors);

        // Temporary recommendations until the algorithm is implemented.
        home.setRecommendedShops(allShops);

        // Temporarily uses highest GutTrust-score shops as GutFriendly Picks.
        home.setGutFriendlyPicks(trustedVendors);

        home.setCategories(
                Arrays.stream(Category.values())
                        .map(Category::name)
                        .toList()
        );

        // User details now come from the database.
        home.setUserName(user.getFname());
        home.setRewardPoints(user.getRewardPoints());

        return home;
    }
}