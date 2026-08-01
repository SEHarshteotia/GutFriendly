package com.gutfriendly.app.inspector.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inspection_images")
public class InspectionImages {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int imageId;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "inspection_id", nullable = false)
	    private InspectionDetails inspection;

	    @Column(nullable = false, length = 500)
	    private String imageUrl;

	    @Column(nullable = false)
	    private boolean primaryImage = false;

}
