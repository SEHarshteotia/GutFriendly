package com.gutfriendly.app.vendor.repository;



import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;



import com.gutfriendly.app.vendor.model.StoreAddress;

import com.gutfriendly.app.vendor.model.Store;



/**

 * Persistence access for {@link StoreAddress} entities.

 */

public interface StoreAddressRepo extends JpaRepository<StoreAddress, Long> {



	Optional<StoreAddress> findByStore(Store store);

}

