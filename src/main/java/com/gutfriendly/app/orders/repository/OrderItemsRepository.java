package com.gutfriendly.app.orders.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.orders.model.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

	@Query("""
			select f.foodName, sum(i.quantity)
			from OrderItems i
			join i.food f
			join i.order o
			where o.shop = :shop
			and o.orderedAt between :start and :end
			and o.status <> com.gutfriendly.app.orders.enums.Status.CANCELLED
			group by f.foodName
			order by sum(i.quantity) desc
			""")
	List<Object[]> findTopSellingItems(@Param("shop") ShopDetails shop,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
