package com.gutfriendly.app.common.validation;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Server side mirror of frontend/shared/validation.js.
 *
 * The browser forms already block weak passwords, fake phone numbers and
 * malformed emails, but those checks only protect people using the UI.
 * Anything that posts straight to the API bypasses them completely, so the
 * same rules are enforced here as well.
 *
 * Every method returns the cleaned value or throws ValidationException, which
 * each caller translates into whichever error type that module already uses.
 */
public final class RegistrationValidator {

	public static final int PASSWORD_MIN_LENGTH = 8;

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");

	private static final Pattern SPECIAL_CHARACTER = Pattern.compile("[^A-Za-z0-9]");

	/** Fragments that make a password guessable no matter how it is composed. */
	private static final String[] COMMON_FRAGMENTS = {
			"password", "gutfriendly", "qwerty", "welcome", "letmein",
			"iloveyou", "admin", "abc123", "12345678", "123456789", "987654321"
	};

	private RegistrationValidator() {
	}

	/** Raised when a submitted value fails a rule. */
	public static class ValidationException extends RuntimeException {

		public ValidationException(String message) {
			super(message);
		}
	}

	/**
	 * Enforces the same four mandatory password rules as the signup forms:
	 * at least 8 characters, one capital letter, one number and one special
	 * character, and no well known fragment.
	 */
	public static String validatePassword(String password) {

		if (password == null || password.isBlank()) {
			throw new ValidationException("Password is required");
		}

		if (password.length() < PASSWORD_MIN_LENGTH) {
			throw new ValidationException(
					"Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
		}

		if (!hasMatch(password, Character::isUpperCase)) {
			throw new ValidationException("Password must contain at least one capital letter");
		}

		if (!hasMatch(password, Character::isDigit)) {
			throw new ValidationException("Password must contain at least one number");
		}

		if (!SPECIAL_CHARACTER.matcher(password).find()) {
			throw new ValidationException("Password must contain at least one special character");
		}

		String lowered = password.toLowerCase(Locale.ROOT);

		for (String fragment : COMMON_FRAGMENTS) {
			if (lowered.contains(fragment)) {
				throw new ValidationException("Password is too easy to guess, please choose another");
			}
		}

		return password;
	}

	/**
	 * Normalizes an Indian mobile number to 10 digits and rejects shapes that
	 * cannot be real: a leading digit outside 6-9, every digit the same, or a
	 * straight ascending or descending run.
	 */
	public static String validateIndianMobile(String phoneNo) {

		if (phoneNo == null || phoneNo.isBlank()) {
			throw new ValidationException("Phone number is required");
		}

		String digits = phoneNo.replaceAll("\\D", "");

		if (digits.length() == 12 && digits.startsWith("91")) {
			digits = digits.substring(2);
		} else if (digits.length() == 11 && digits.startsWith("0")) {
			digits = digits.substring(1);
		}

		if (digits.length() != 10) {
			throw new ValidationException("Enter a valid 10-digit Indian mobile number");
		}

		char first = digits.charAt(0);

		if (first < '6' || first > '9') {
			throw new ValidationException("Indian mobile numbers start with 6, 7, 8 or 9");
		}

		if (digits.chars().distinct().count() == 1) {
			throw new ValidationException("That phone number does not look real");
		}

		if (isSequential(digits)) {
			throw new ValidationException("That phone number does not look real");
		}

		return digits;
	}

	/** Validates an email address. Returns null when blank and not required. */
	public static String validateEmail(String email, boolean required) {

		if (email == null || email.isBlank()) {

			if (required) {
				throw new ValidationException("Email is required");
			}

			return null;
		}

		String trimmed = email.trim();

		if (trimmed.length() > 254) {
			throw new ValidationException("Email address is too long");
		}

		if (trimmed.contains("..") || !EMAIL_PATTERN.matcher(trimmed).matches()) {
			throw new ValidationException("Enter a valid email address");
		}

		return trimmed.toLowerCase(Locale.ROOT);
	}

	private static boolean hasMatch(String value, java.util.function.IntPredicate test) {
		return value.chars().anyMatch(test);
	}

	private static boolean isSequential(String digits) {

		boolean ascending = true;
		boolean descending = true;

		for (int i = 1; i < digits.length(); i++) {

			int step = digits.charAt(i) - digits.charAt(i - 1);

			if (step != 1) {
				ascending = false;
			}

			if (step != -1) {
				descending = false;
			}
		}

		return ascending || descending;
	}
}
