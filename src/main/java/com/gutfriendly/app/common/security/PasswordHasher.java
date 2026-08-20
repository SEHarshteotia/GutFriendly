package com.gutfriendly.app.common.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Stores and verifies passwords as salted PBKDF2-HMAC-SHA256 digests.
 *
 * <p>Format: {@code pbkdf2$sha256$<iterations>$<saltBase64>$<hashBase64>}
 *
 * <p>Every account created before hashing existed still holds a cleartext
 * password. {@link #matches(String, String)} therefore falls back to a
 * constant-time literal comparison when the stored value is not in the format
 * above, so existing logins keep working. Callers should follow a successful
 * check with {@link #needsRehash(String)} and re-save the upgraded digest, which
 * retires each legacy row the first time its owner signs in.
 *
 * <p>Uses only {@code javax.crypto} so no new dependency is introduced. In
 * particular this avoids {@code spring-boot-starter-security}, which would
 * auto-configure a filter chain over every endpoint.
 */
public final class PasswordHasher {

	private static final String PREFIX = "pbkdf2$sha256$";
	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 120_000;
	private static final int SALT_BYTES = 16;
	private static final int KEY_BITS = 256;

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();

	private PasswordHasher() {
	}

	/** Returns the encoded digest for a raw password. */
	public static String hash(String rawPassword) {
		if (rawPassword == null) {
			throw new IllegalArgumentException("Password is required");
		}

		byte[] salt = new byte[SALT_BYTES];
		RANDOM.nextBytes(salt);
		byte[] digest = pbkdf2(rawPassword, salt, ITERATIONS);

		return PREFIX + ITERATIONS + "$"
				+ ENCODER.encodeToString(salt) + "$"
				+ ENCODER.encodeToString(digest);
	}

	/**
	 * Verifies a raw password against a stored value, accepting both hashed and
	 * legacy cleartext rows. Returns false rather than throwing on malformed
	 * input so a corrupt row cannot crash a login.
	 */
	public static boolean matches(String rawPassword, String storedValue) {
		if (rawPassword == null || storedValue == null || storedValue.isEmpty()) {
			return false;
		}

		if (!isHashed(storedValue)) {
			// Legacy cleartext row, still constant time.
			return MessageDigest.isEqual(
					rawPassword.getBytes(StandardCharsets.UTF_8),
					storedValue.getBytes(StandardCharsets.UTF_8));
		}

		String[] parts = storedValue.split("\\$");
		if (parts.length != 5) {
			return false;
		}

		try {
			int iterations = Integer.parseInt(parts[2]);
			byte[] salt = DECODER.decode(parts[3]);
			byte[] expected = DECODER.decode(parts[4]);
			byte[] actual = pbkdf2(rawPassword, salt, iterations);
			return MessageDigest.isEqual(expected, actual);
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	/** True when a stored value is cleartext and should be re-saved as a hash. */
	public static boolean needsRehash(String storedValue) {
		return storedValue == null || !isHashed(storedValue);
	}

	private static boolean isHashed(String storedValue) {
		return storedValue.startsWith(PREFIX);
	}

	private static byte[] pbkdf2(String rawPassword, byte[] salt, int iterations) {
		try {
			KeySpec spec = new PBEKeySpec(
					rawPassword.toCharArray(), salt, iterations, KEY_BITS);
			return SecretKeyFactory.getInstance(ALGORITHM)
					.generateSecret(spec)
					.getEncoded();
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to hash password", ex);
		}
	}
}
