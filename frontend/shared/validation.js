/**
 * Shared signup validation for the GutFriendly frontends.
 *
 * Every app (userside, vendor, admin-inspector) already aliases this folder
 * as "@shared" in its vite config, so all three import the same rules and
 * cannot drift apart.
 *
 * These checks are a usability layer, not a security boundary. The same rules
 * are enforced again on the server in RegistrationValidator.java, because
 * anything in this file can be bypassed by posting straight to the API.
 */

export const PASSWORD_MIN_LENGTH = 8;

const SPECIAL_CHARACTER = /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?~`]/;

/* Substrings common enough that the password is guessable whatever its shape. */
const COMMON_FRAGMENTS = [
    "password",
    "gutfriendly",
    "qwerty",
    "welcome",
    "letmein",
    "iloveyou",
    "admin",
    "abc123",
    "12345678",
    "123456789",
    "987654321"
];

export const PASSWORD_RULES = [
    {
        id: "length",
        label: "At least " + PASSWORD_MIN_LENGTH + " characters",
        test: (value) => value.length >= PASSWORD_MIN_LENGTH
    },
    {
        id: "uppercase",
        label: "One capital letter (A\u2013Z)",
        test: (value) => /[A-Z]/.test(value)
    },
    {
        id: "number",
        label: "One number (0\u20139)",
        test: (value) => /[0-9]/.test(value)
    },
    {
        id: "special",
        label: "One special character (! @ # $ \u2026)",
        test: (value) => SPECIAL_CHARACTER.test(value)
    }
];

function containsCommonFragment(value) {
    const flattened = value.toLowerCase().replace(/[^a-z0-9]/g, "");

    return COMMON_FRAGMENTS.some(
        (fragment) => flattened.includes(fragment)
    );
}

/**
 * Grades a password.
 *
 * The four rules above are mandatory: miss any one and the result is "weak"
 * and rejected. Meeting all four earns "medium", which is the minimum we
 * accept. "strong" is awarded for extra length and character variety on top.
 *
 * Returns { level, label, score, percent, rules, unmet, isAcceptable, message }.
 */
export function evaluatePassword(rawValue) {
    const value = typeof rawValue === "string" ? rawValue : "";

    const rules = PASSWORD_RULES.map((rule) => ({
        id: rule.id,
        label: rule.label,
        met: rule.test(value)
    }));

    const unmet = rules.filter((rule) => !rule.met);

    if (value.length === 0) {
        return {
            level: "empty",
            label: "",
            score: 0,
            percent: 0,
            rules,
            unmet,
            isAcceptable: false,
            message: "Password is required."
        };
    }

    if (unmet.length > 0) {
        return {
            level: "weak",
            label: "Weak",
            score: 1,
            percent: 33,
            rules,
            unmet,
            isAcceptable: false,
            message: "Password is too weak: it needs " +
                unmet.map((rule) => rule.label.toLowerCase()).join(", ") + "."
        };
    }

    if (containsCommonFragment(value)) {
        return {
            level: "weak",
            label: "Weak",
            score: 1,
            percent: 33,
            rules,
            unmet,
            isAcceptable: false,
            message: "Password contains a commonly guessed word. Please choose something less predictable."
        };
    }

    let bonus = 0;

    if (value.length >= 12) {
        bonus += 1;
    }

    if (value.length >= 16) {
        bonus += 1;
    }

    if (/[a-z]/.test(value)) {
        bonus += 1;
    }

    if (new Set(value).size >= 8) {
        bonus += 1;
    }

    if (bonus >= 3) {
        return {
            level: "strong",
            label: "Strong",
            score: 3,
            percent: 100,
            rules,
            unmet,
            isAcceptable: true,
            message: ""
        };
    }

    return {
        level: "medium",
        label: "Medium",
        score: 2,
        percent: 66,
        rules,
        unmet,
        isAcceptable: true,
        message: ""
    };
}

/**
 * Normalizes an Indian mobile number to its 10 significant digits.
 * Accepts "9876501234", "+91 98765 01234", "919876501234" and "09876501234".
 * Returns null when the input cannot be read as one.
 */
export function normalizeIndianPhone(rawValue) {
    if (typeof rawValue !== "string") {
        return null;
    }

    let digits = rawValue.replace(/\D/g, "");

    if (digits.length === 12 && digits.startsWith("91")) {
        digits = digits.slice(2);
    } else if (digits.length === 11 && digits.startsWith("0")) {
        digits = digits.slice(1);
    }

    return digits.length === 10 ? digits : null;
}

function isAllSameDigit(digits) {
    return /^(\d)\1{9}$/.test(digits);
}

function isSequential(digits) {
    let ascending = true;
    let descending = true;

    for (let index = 1; index < digits.length; index += 1) {
        const step = Number(digits[index]) - Number(digits[index - 1]);

        if (step !== 1) {
            ascending = false;
        }

        if (step !== -1) {
            descending = false;
        }
    }

    return ascending || descending;
}

/**
 * Validates an Indian mobile number.
 *
 * A real number is 10 digits and starts with 6, 7, 8 or 9 - the ranges TRAI
 * allocates to mobile operators. On top of that we reject the shapes people
 * type when they are making a number up: all-identical digits (9999999999)
 * and straight runs (9876543210).
 *
 * Returns { isValid, value, message }.
 */
export function validateIndianPhone(rawValue) {
    const digits = normalizeIndianPhone(rawValue);

    if (digits === null) {
        return {
            isValid: false,
            value: "",
            message: "Enter a 10-digit Indian mobile number."
        };
    }

    if (!/^[6-9]/.test(digits)) {
        return {
            isValid: false,
            value: digits,
            message: "Indian mobile numbers start with 6, 7, 8 or 9."
        };
    }

    if (isAllSameDigit(digits) || isSequential(digits)) {
        return {
            isValid: false,
            value: digits,
            message: "That does not look like a real mobile number."
        };
    }

    return { isValid: true, value: digits, message: "" };
}

/*
 * Deliberately stricter than the browser's built-in type="email" check, which
 * accepts things like "a@b". Requires a sane local part, a dotted domain and a
 * TLD of at least two letters, and rejects doubled or edge dots.
 */
const EMAIL_PATTERN = /^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)*\.[A-Za-z]{2,}$/;

/**
 * Validates an email address.
 * Pass { required: false } for the optional email fields.
 * Returns { isValid, value, message }.
 */
export function validateEmail(rawValue, options) {
    const required = !options || options.required !== false;
    const value = typeof rawValue === "string" ? rawValue.trim() : "";

    if (value.length === 0) {
        return required
            ? { isValid: false, value: "", message: "Email address is required." }
            : { isValid: true, value: "", message: "" };
    }

    if (value.length > 254 || value.includes("..") || !EMAIL_PATTERN.test(value)) {
        return {
            isValid: false,
            value,
            message: "Enter a valid email address, for example name@example.com."
        };
    }

    return { isValid: true, value, message: "" };
}
