package com.gutfriendly.app.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Issues and verifies the stateless session tokens used by every portal.
 *
 * <p>Format: {@code base64url(payload).base64url(hmacSha256(payload))} where the
 * payload is the plain string {@code ROLE:subjectId:expiryEpochSeconds}. This is
 * a JWT in spirit but deliberately hand rolled: it keeps the dependency list
 * unchanged and avoids parsing JSON on a path that runs before Jackson is even
 * involved.
 *
 * <p>The signing secret comes from the {@code APP_AUTH_SECRET} environment
 * variable. Without it a well known development secret is used and a warning is
 * logged on every startup, because tokens signed with it are trivially forgeable.
 */
@Service
public class AuthTokenService {

	private static final Logger log = LoggerFactory.getLogger(AuthTokenService.class);

	private static final String DEV_SECRET = "gutfriendly-local-development-secret-do-not-use-in-production";

	private static final String HMAC_ALGORITHM = "HmacSHA256";

	/** Seven days. Long enough that nobody is logged out mid task. */
	private static final long TOKEN_TTL_SECONDS = 7L * 24 * 60 * 60;

	private final byte[] secret;

	public AuthTokenService(@Value("${app.auth.secret:}") String configuredSecret) {
		String resolved = configuredSecret == null ? "" : configuredSecret.trim();

		if (resolved.isEmpty()) {
			log.warn("APP_AUTH_SECRET is not set - falling back to the development signing secret. "
					+ "Set APP_AUTH_SECRET in the deployment environment before going to production.");
			resolved = DEV_SECRET;
		}

		this.secret = resolved.getBytes(StandardCharsets.UTF_8);
	}

	/** Builds a signed token for the given role and account id. */
	public String issue(AuthRole role, Object subjectId) {
		long expiry = Instant.now().getEpochSecond() + TOKEN_TTL_SECONDS;
		String payload = role.name() + ":" + String.valueOf(subjectId) + ":" + expiry;

		return encode(payload.getBytes(StandardCharsets.UTF_8)) + "." + encode(sign(payload));
	}

	/**
	 * Verifies the signature and expiry of a token.
	 *
	 * @return the caller it identifies, or {@code null} if the token is missing,
	 *         malformed, tampered with, or expired
	 */
	public AuthPrincipal verify(String token) {
		if (token == null || token.isBlank()) {
			return null;
		}

		int dot = token.indexOf('.');

		if (dot <= 0 || dot == token.length() - 1) {
			return null;
		}

		String payload;
		byte[] providedSignature;

		try {
			payload = new String(decode(token.substring(0, dot)), StandardCharsets.UTF_8);
			providedSignature = decode(token.substring(dot + 1));
		} catch (IllegalArgumentException malformed) {
			return null;
		}

		// Constant time comparison: a byte-by-byte early exit would leak the
		// signature one character at a time.
		if (!MessageDigest.isEqual(sign(payload), providedSignature)) {
			return null;
		}

		String[] parts = payload.split(":");

		if (parts.length != 3) {
			return null;
		}

		AuthRole role = AuthRole.fromName(parts[0]);

		if (role == null) {
			return null;
		}

		long expiry;

		try {
			expiry = Long.parseLong(parts[2]);
		} catch (NumberFormatException notANumber) {
			return null;
		}

		if (Instant.now().getEpochSecond() >= expiry) {
			return null;
		}

		return new AuthPrincipal(role, parts[1]);
	}

	private byte[] sign(String payload) {
		try {
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));

			return mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
		} catch (Exception impossible) {
			// HmacSHA256 is guaranteed present on every JDK, and the key is never empty.
			throw new IllegalStateException("Unable to sign authentication token", impossible);
		}
	}

	private static String encode(byte[] value) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
	}

	private static byte[] decode(String value) {
		return Base64.getUrlDecoder().decode(value);
	}
}
