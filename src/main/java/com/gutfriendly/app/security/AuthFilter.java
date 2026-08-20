package com.gutfriendly.app.security;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The single gate in front of every API call.
 *
 * <p>Before this filter existed the portals were protected only by
 * {@code localStorage.role} in the browser, which anyone could set by hand.
 * Every rule below is expressed as a path pattern rather than an annotation so
 * that the whole policy can be read in one place.
 *
 * <p>Two kinds of check are applied:
 * <ul>
 * <li><b>Role</b> - an inspector cannot reach {@code /admin/**} at all.</li>
 * <li><b>Ownership</b> - a signed in user cannot read another user's profile by
 * changing the id in the URL, which is the IDOR hole this closes.</li>
 * </ul>
 */
@Component
@Order(1)
public class AuthFilter extends OncePerRequestFilter {

	/** {@code /cart/user/12}, {@code /orders/user/12/7}, {@code /home/user/12} ... */
	private static final Pattern USER_SCOPED = Pattern.compile("^/(?:cart|wishlist|orders|reviews|home)/user/(\\d+)(?:/.*)?$");

	/** {@code /users/profile/12}, {@code /users/address/12}, {@code /users/12} ... */
	private static final Pattern USERS_WITH_ID = Pattern.compile("^/users/(?:profile/|address/)?(\\d+)(?:/.*)?$");

	/** {@code /vendor/4/shops/9/menu} - the first segment is the vendor id. */
	private static final Pattern VENDOR_SCOPED = Pattern.compile("^/vendor/(\\d+)(?:/.*)?$");

	/** {@code /inspector/3/inspections} - only that inspector's own worklist. */
	private static final Pattern INSPECTOR_SCOPED = Pattern.compile("^/inspector/(\\d+)(?:/.*)?$");

	private final AuthTokenService tokens;

	public AuthFilter(AuthTokenService tokens) {
		this.tokens = tokens;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		String method = request.getMethod();

		if (isPublic(path, method)) {
			chain.doFilter(request, response);
			return;
		}

		AuthPrincipal principal = tokens.verify(bearerToken(request));

		if (principal == null) {
			reject(response, HttpStatus.UNAUTHORIZED, "Your session has expired. Please sign in again.", path);
			return;
		}

		String denial = denialReason(path, method, principal);

		if (denial != null) {
			reject(response, HttpStatus.FORBIDDEN, denial, path);
			return;
		}

		request.setAttribute("authPrincipal", principal);
		chain.doFilter(request, response);
	}

	/**
	 * Anything reachable without signing in: the login and registration forms,
	 * plus the public catalogue that the landing page shows to guests.
	 */
	private boolean isPublic(String path, String method) {
		if ("OPTIONS".equalsIgnoreCase(method)) {
			// CORS preflight never carries the Authorization header.
			return true;
		}

		if (path.equals("/error") || path.startsWith("/actuator/health")) {
			return true;
		}

		if (path.equals("/users/login") || path.equals("/users/register")
				|| path.equals("/vendor/login") || path.equals("/vendor/register")
				|| path.equals("/admin/login")
				|| path.equals("/inspector/login")) {
			return true;
		}

		if ("GET".equalsIgnoreCase(method)) {
			// Browsing shops, menus and public reviews needs no account.
			return path.equals("/shops") || path.startsWith("/shops/")
					|| path.equals("/foods") || path.startsWith("/foods/")
					|| isPublicReview(path);
		}

		return false;
	}

	/** Shop review listings are public; {@code /reviews/user/**} is not. */
	private boolean isPublicReview(String path) {
		return path.startsWith("/reviews/shop/")
				|| (path.startsWith("/reviews/") && !path.startsWith("/reviews/user/"));
	}

	/**
	 * @return {@code null} when the caller may proceed, otherwise the message to
	 *         show them
	 */
	private String denialReason(String path, String method, AuthPrincipal principal) {
		if (path.startsWith("/admin")) {
			return principal.role() == AuthRole.ADMIN ? null : "Administrator access is required for this action.";
		}

		if (path.startsWith("/inspector")) {
			if (principal.role() == AuthRole.ADMIN) {
				return null;
			}

			if (principal.role() != AuthRole.INSPECTOR) {
				return "Inspector access is required for this action.";
			}

			Matcher scoped = INSPECTOR_SCOPED.matcher(path);

			if (scoped.matches() && !principal.owns(scoped.group(1))) {
				return "You can only view your own inspection worklist.";
			}

			return null;
		}

		if (path.startsWith("/vendor")) {
			if (principal.role() != AuthRole.VENDOR) {
				return "Vendor access is required for this action.";
			}

			Matcher scoped = VENDOR_SCOPED.matcher(path);

			if (scoped.matches() && !principal.owns(scoped.group(1))) {
				return "You can only manage your own shops.";
			}

			return null;
		}

		// A vendor marking an order as delivered, or an admin correcting one.
		if (path.startsWith("/orders/") && path.endsWith("/status")) {
			return principal.role() == AuthRole.VENDOR || principal.role() == AuthRole.ADMIN
					? null
					: "Only the shop owner can change an order's status.";
		}

		Matcher userScoped = USER_SCOPED.matcher(path);

		if (userScoped.matches()) {
			return ownershipDenial(principal, userScoped.group(1));
		}

		Matcher usersWithId = USERS_WITH_ID.matcher(path);

		if (usersWithId.matches()) {
			return ownershipDenial(principal, usersWithId.group(1));
		}

		if (path.startsWith("/users") || path.startsWith("/cart") || path.startsWith("/wishlist")
				|| path.startsWith("/orders") || path.startsWith("/reviews") || path.startsWith("/home")) {
			return principal.role() == AuthRole.USER ? null : "This action is only available to signed in customers.";
		}

		// Anything not named above stays closed rather than accidentally open.
		return "You do not have permission to perform this action.";
	}

	private String ownershipDenial(AuthPrincipal principal, String pathId) {
		if (principal.role() == AuthRole.ADMIN) {
			return null;
		}

		if (principal.role() != AuthRole.USER) {
			return "This action is only available to signed in customers.";
		}

		return principal.owns(pathId) ? null : "You can only access your own account.";
	}

	private String bearerToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null) {
			return null;
		}

		String trimmed = header.trim();

		return trimmed.regionMatches(true, 0, "Bearer ", 0, 7) ? trimmed.substring(7).trim() : trimmed;
	}

	/** Mirrors the shape produced by GlobalExceptionHandler so the clients parse it. */
	private void reject(HttpServletResponse response, HttpStatus status, String message, String path)
			throws IOException {

		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"timestamp\":\"" + java.time.LocalDateTime.now() + "\","
				+ "\"status\":" + status.value() + ","
				+ "\"error\":\"" + status.getReasonPhrase() + "\","
				+ "\"message\":\"" + escape(message) + "\","
				+ "\"path\":\"" + escape(path) + "\"}");
	}

	private static String escape(String value) {
		return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
