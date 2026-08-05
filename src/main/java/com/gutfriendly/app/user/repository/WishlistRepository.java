package com.gutfriendly.app.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.user.model.Wishlist;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Integer> {

    @Query("""
            SELECT w
            FROM Wishlist w
            WHERE w.user.user_id = :userId
            ORDER BY w.createdAt DESC
            """)
    List<Wishlist> findWishlistByUserId(
            @Param("userId") int userId
    );

    @Query("""
            SELECT w
            FROM Wishlist w
            WHERE w.user.user_id = :userId
              AND w.shop.shopId = :shopId
            """)
    Optional<Wishlist> findByUserIdAndShopId(
            @Param("userId") int userId,
            @Param("shopId") int shopId
    );

    @Query("""
            SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END
            FROM Wishlist w
            WHERE w.user.user_id = :userId
              AND w.shop.shopId = :shopId
            """)
    boolean existsByUserIdAndShopId(
            @Param("userId") int userId,
            @Param("shopId") int shopId
    );
}