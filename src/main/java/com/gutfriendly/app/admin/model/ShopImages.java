package com.gutfriendly.app.admin.model;

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
@Table(name = "shop_images")
public class ShopImages {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int imageId;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "shop_id", nullable = false)
	    private ShopDetails shop;

	    @Column(nullable = false, length = 500)
	    private String imageUrl;

	    @Column(nullable = false)
	    private boolean primaryImage = false;
	}


