package com.gutfriendly.app.vendor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.MenuItem;
import com.gutfriendly.app.vendor.model.Store;

/**
 * Persistence access for {@link MenuItem} entities.
 */
public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

	List<MenuItem> findByStoreOrderByCategoryAscNameAsc(Store store);

	List<MenuItem> findByStoreAndActiveTrueOrderByCategoryAscNameAsc(Store store);

	Optional<MenuItem> findByItemIdAndStore(Long itemId, Store store);
}
