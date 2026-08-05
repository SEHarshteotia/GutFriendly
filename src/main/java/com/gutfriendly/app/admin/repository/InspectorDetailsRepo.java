package com.gutfriendly.app.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.inspector.model.InspectorDetails;

public interface InspectorDetailsRepo extends JpaRepository<InspectorDetails, Integer> {

	Optional<InspectorDetails> findByInspectorId(int inspectorId);

	Optional<InspectorDetails> findByEmail(String email);

}
