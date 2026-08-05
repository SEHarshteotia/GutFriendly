package com.gutfriendly.app.orders.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.orders.enums.Status;
import com.gutfriendly.app.orders.model.UserOrders;

public interface UserOrdersRepository extends JpaRepository<UserOrders, Integer> {

	long countByShopAndOrderedAtBetween(ShopDetails shop, LocalDateTime start, LocalDateTime end);

	long countByShopAndStatusIn(ShopDetails shop, Collection<Status> statuses);

	List<UserOrders> findTop5ByShopAndStatusInOrderByOrderedAtDesc(ShopDetails shop, Collection<Status> statuses);

	List<UserOrders> findByShopAndStatusInOrderByOrderedAtDesc(ShopDetails shop, Collection<Status> statuses);

	List<UserOrders> findByShopOrderByOrderedAtDesc(ShopDetails shop);

	Optional<UserOrders> findByOrderIdAndShop(int orderId, ShopDetails shop);

	@Query("""
			SELECT o
			FROM UserOrders o
			WHERE o.user.user_id = :userId
			ORDER BY o.orderedAt DESC
			""")
	List<UserOrders> findOrdersByUserId(@Param("userId") int userId);

	@Query("""
			select coalesce(sum(i.itemTotal), 0)
			from OrderItems i
			join i.order o
			where o.shop = :shop
			and o.orderedAt between :start and :end
			and o.status <> com.gutfriendly.app.orders.enums.Status.CANCELLED
			""")
	BigDecimal sumRevenueByShopAndOrderedAtBetween(@Param("shop") ShopDetails shop,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	@Query("""
			select hour(o.orderedAt), count(distinct o.orderId), coalesce(sum(i.itemTotal), 0)
			from UserOrders o
			join o.orderItems i
			where o.shop = :shop
			and o.orderedAt between :start and :end
			and o.status <> com.gutfriendly.app.orders.enums.Status.CANCELLED
			group by hour(o.orderedAt)
			order by hour(o.orderedAt)
			""")
	List<Object[]> findHourlyOrderStats(@Param("shop") ShopDetails shop,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
