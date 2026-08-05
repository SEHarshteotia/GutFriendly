package com.gutfriendly.app.vendor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A pincode where the platform offers delivery or vendor onboarding.
 * <p>
 * Used to validate shop addresses during location save and serviceability recheck.
 */
@Entity
@Table(name = "serviceable_area")
@Data
@NoArgsConstructor
public class ServiceableArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String pincode;

}
