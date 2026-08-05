package com.gutfriendly.app.user.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.ShopDetails;
import com.gutfriendly.app.user.repository.ShopDetailsRepository;

@Service
public class GutTrustScoreService {

    @Autowired
    private ShopDetailsRepository shopRepo;

    // Recalculates final GutTrust score for one shop.
    @Transactional
    public double recalculateFinalScore(int shopId) {

        ShopDetails shop = shopRepo.findById(shopId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shop not found"
                        ));

        double userScore =
                shop.getUserTrustScore() == null
                        ? 0.0
                        : shop.getUserTrustScore();

        double inspectionScore =
                shop.getInspectionTrustScore() == null
                        ? 0.0
                        : shop.getInspectionTrustScore();

        double finalScore;

        if (inspectionScore == 0.0) {
            finalScore = userScore;
        } else if (userScore == 0.0) {
            finalScore = inspectionScore;
        } else {
            finalScore =
                    inspectionScore * 0.70
                    + userScore * 0.30;
        }

        finalScore =
                Math.round(finalScore * 100.0) / 100.0;

        shop.setFinalGutTrustScore(finalScore);
        shop.setLastCalculatedAt(LocalDateTime.now());

        shopRepo.save(shop);

        return finalScore;
    }
}