package com.gutfriendly.app.security;

/** The four kinds of account that can hold a session token. */
public enum AuthRole {

	USER,
	VENDOR,
	INSPECTOR,
	ADMIN;

	/** Case insensitive lookup that returns {@code null} instead of throwing. */
	public static AuthRole fromName(String name) {
		if (name == null) {
			return null;
		}

		for (AuthRole role : values()) {
			if (role.name().equalsIgnoreCase(name.trim())) {
				return role;
			}
		}

		return null;
	}
}
