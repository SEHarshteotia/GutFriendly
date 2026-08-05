package com.gutfriendly.app.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.user.model.CustomerOrder;

public interface CustomerOrderRepository
        extends JpaRepository<CustomerOrder, Integer> {

    @Query("""
            SELECT o
            FROM CustomerOrder o
            WHERE o.user.user_id = :userId
            ORDER BY o.orderedAt DESC
            """)
    List<CustomerOrder> findOrdersByUserId(
            @Param("userId") int userId
    );
}