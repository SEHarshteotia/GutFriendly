package com.gutfriendly.app.admin.dto.response;

public class InspectorSummaryResponse {

	private int inspectorId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNo;
	private String status;

	public InspectorSummaryResponse() {
	}

	public InspectorSummaryResponse(
			int inspectorId,
			String firstName,
			String lastName,
			String email,
			String phoneNo,
			String status) {

		this.inspectorId = inspectorId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNo = phoneNo;
		this.status = status;
	}

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

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
