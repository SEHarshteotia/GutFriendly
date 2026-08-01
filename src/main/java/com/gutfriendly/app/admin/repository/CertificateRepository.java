package com.gutfriendly.app.admin.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.Certificate;

public interface CertificateRepository  extends JpaRepository<Certificate, Integer>{
	
	Page<Certificate> findAllByOrderByIssueDateDesc(Pageable pageable);

}
