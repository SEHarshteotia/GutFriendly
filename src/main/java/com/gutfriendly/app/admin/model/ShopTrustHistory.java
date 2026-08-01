package com.gutfriendly.app.admin.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ShopTrustHistory {
        @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long historyId;

	    @ManyToOne
	    private ShopDetails shop;

	    private Double userTrustScore;

	    private Double inspectionTrustScore;

	    private Double finalGutTrustScore;

	    private Timestamp calculatedAt;

	    private String calculatedBy;
	}

