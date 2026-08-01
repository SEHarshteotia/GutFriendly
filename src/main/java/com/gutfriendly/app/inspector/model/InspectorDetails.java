package com.gutfriendly.app.inspector.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.gutfriendly.app.admin.enums.InspectorDesignation;
import com.gutfriendly.app.admin.enums.InspectorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "inspector_details")
public class InspectorDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int inspectorId;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(nullable = false, unique = true, length = 10)
	private String employeeCode;

	@Column(nullable = false, unique = true, length = 15)
	private String phoneNo;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InspectorStatus status = InspectorStatus.ACTIVE;

	@Column(nullable = false)
	private Integer experienceInYears = 0;

	@Column(length = 100)
	private String assignedCity;

	@Column(length = 100)
	private String assignedZone;

	@Column(length = 100)
	private InspectorDesignation designation = InspectorDesignation.SENIOR_INSPECTOR;

	@Column(length = 500)
	private String address;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime joiningDate;

	@OneToMany(mappedBy = "inspector")
	private List<InspectionDetails> inspections;

	public int getInspectorId() {
		return inspectorId;
	}

	public void setInspectorId(int inspectorId) {
		this.inspectorId = inspectorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public InspectorStatus getStatus() {
		return status;
	}

	public void setStatus(InspectorStatus status) {
		this.status = status;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getExperienceInYears() {
		return experienceInYears;
	}

	public void setExperienceInYears(Integer experienceInYears) {
		this.experienceInYears = experienceInYears;
	}

	public String getAssignedCity() {
		return assignedCity;
	}

	public void setAssignedCity(String assignedCity) {
		this.assignedCity = assignedCity;
	}

	public String getAssignedZone() {
		return assignedZone;
	}

	public void setAssignedZone(String assignedZone) {
		this.assignedZone = assignedZone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDateTime joiningDate) {
		this.joiningDate = joiningDate;
	}

	public List<InspectionDetails> getInspections() {
		return inspections;
	}

	public void setInspections(List<InspectionDetails> inspections) {
		this.inspections = inspections;
	}

	public InspectorDesignation getDesignation() {
		return designation;
	}

	public void setDesignation(InspectorDesignation designation) {
		this.designation = designation;
	}

}
