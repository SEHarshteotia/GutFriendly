package com.gutfriendly.app.admin.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.inspector.model.InspectionDetails;

public interface InspectionDetailsRepository extends JpaRepository<InspectionDetails, Integer> {
	
	@Query("SELECT COUNT(i) FROM InspectionDetails i WHERE i.status = 'SCHEDULED'")
	long countScheduledInspections();

	boolean existsByShop_ShopIdAndStatusIn(int shopId, Collection<InspectionStatus> statuses);

	boolean existsByShop_ShopIdAndStatus(int shopId, InspectionStatus status);

	Optional<InspectionDetails> findFirstByShop_ShopIdAndStatusInOrderByInspectionDateDesc(int shopId,
			Collection<InspectionStatus> statuses);

	Optional<InspectionDetails> findFirstByShop_ShopIdAndStatusInOrderByCompletedAtDescInspectionDateDesc(
			int shopId, Collection<InspectionStatus> statuses);
	
	Page<InspectionDetails> findAllByOrderByInspectionDateDesc(Pageable pageable);
	
	
	Page<InspectionDetails> findByStatusAndInspectionDateAfterOrderByInspectionDateAsc(
	        InspectionStatus status,
	        LocalDateTime inspectionDate,
	        Pageable pageable);
	
	
	  Page<InspectionDetails> findByStatus(
	            InspectionStatus status,
	            Pageable pageable);

	    Page<InspectionDetails> findByShop_ShopId(
	            int shopId,
	            Pageable pageable);

	    Page<InspectionDetails> findByInspector_InspectorId(
	            int inspectorId,
	            Pageable pageable);
	

}
