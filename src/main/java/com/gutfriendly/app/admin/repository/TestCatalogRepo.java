package com.gutfriendly.app.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.inspector.model.TestCatalog;

public interface TestCatalogRepo extends JpaRepository<TestCatalog, Integer> {
	
	List<TestCatalog> findByActiveTrue();
	

}
