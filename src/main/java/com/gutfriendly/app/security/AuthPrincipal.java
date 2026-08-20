package com.gutfriendly.app.security;

/**
 * The caller behind a verified token.
 *
 * @param role      what the caller is allowed to reach
 * @param subjectId the account id, kept as text because user, vendor, inspector
 *                  and admin ids are not all the same Java type
 */
public record AuthPrincipal(AuthRole role, String subjectId) {

	/** True when this principal owns the account id taken from a request path. */
	public boolean owns(String pathId) {
		return subjectId != null && subjectId.equals(pathId);
	}
}
