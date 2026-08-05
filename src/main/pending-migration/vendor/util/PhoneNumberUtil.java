package com.gutfriendly.app.vendor.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Normalizes Indian mobile numbers to 10-digit format for storage and lookup.
 */
public final class PhoneNumberUtil {

	private PhoneNumberUtil() {
	}

	/**
	 * Normalizes phone input to a 10-digit Indian mobile number.
	 * Accepts formats like {@code 9876543210}, {@code +91 98765 43210}, {@code 919876543210}.
	 */
	public static String normalize(String phoneNo) {
		if (phoneNo == null || phoneNo.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");
		}

		String digits = phoneNo.replaceAll("\\D", "");

		if (digits.length() == 12 && digits.startsWith("91")) {
			digits = digits.substring(2);
		} else if (digits.length() == 11 && digits.startsWith("0")) {
			digits = digits.substring(1);
		}

		if (digits.length() != 10) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Phone number must be a valid 10-digit Indian mobile number");
		}

		return digits;
	}
}
