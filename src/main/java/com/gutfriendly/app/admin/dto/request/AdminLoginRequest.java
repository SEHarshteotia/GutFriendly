package com.gutfriendly.app.admin.dto.request;

/**
 * Credentials posted to {@code POST /admin/login}.
 *
 * <p>Kept separate from the {@code AdminDetails} entity so the login endpoint
 * cannot be used to bind — and therefore overwrite — arbitrary entity fields.
 */
public class AdminLoginRequest {

	private String email;

	private String password;

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
}
