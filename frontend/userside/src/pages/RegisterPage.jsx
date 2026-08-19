import { ArrowLeft } from "lucide-react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import GutFriendlyLogo from "@shared/GutFriendlyLogo";
import PasswordStrengthMeter from "@shared/PasswordStrengthMeter";
import {
  evaluatePassword,
  validateEmail,
  validateIndianPhone,
} from "@shared/validation";
import { registerUser } from "../services/authService";

const FIELD_ERROR_STYLE = {
  margin: "6px 0 0",
  fontSize: "12px",
  color: "#dc2626",
};

function RegisterPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    fname: "",
    lname: "",
    phoneNo: "",
    email: "",
    password: "",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [touched, setTouched] = useState({});

  const passwordCheck = evaluatePassword(formData.password);
  const phoneCheck = validateIndianPhone(formData.phoneNo);
  const emailCheck = validateEmail(formData.email);

  const canSubmit =
    passwordCheck.isAcceptable &&
    phoneCheck.isValid &&
    emailCheck.isValid;

  function handleBlur(event) {
    const { name } = event.target;

    setTouched((previous) => ({ ...previous, [name]: true }));
  }

  function handleChange(event) {
    const { name, value } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    setMessage("");
    setError("");

    setTouched({ phoneNo: true, email: true, password: true });

    if (!phoneCheck.isValid) {
      setError(phoneCheck.message);
      return;
    }

    if (!emailCheck.isValid) {
      setError(emailCheck.message);
      return;
    }

    if (!passwordCheck.isAcceptable) {
      setError(passwordCheck.message);
      return;
    }

    setLoading(true);

    const registrationData = {
      ...formData,
      phoneNo: phoneCheck.value,
      email: emailCheck.value,
      joining_date: new Date()
        .toISOString()
        .slice(0, 19),
      is_active: true,
      trustedUser: false,
      rewardPoints: 0,
    };

    try {
      await registerUser(registrationData);

      setMessage(
        "Account created successfully. Redirecting to login..."
      );

      setTimeout(() => {
        navigate("/login");
      }, 1000);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-visual">
        <Link to="/" className="auth-back-link">
          <ArrowLeft size={18} />
          Back to home
        </Link>

        <div className="auth-visual-content">
          <Link
            to="/"
            className="auth-logo-link"
            aria-label="Go to GutFriendly home"
          >
            <GutFriendlyLogo
              size="lg"
              theme="onDark"
              showWordmark={false}
            />
          </Link>

          <h1>Make safer food choices every day.</h1>

          <p>
            Create your GutFriendly account to save trusted
            shops, place orders, write verified reviews and
            earn reward points.
          </p>
        </div>
      </div>

      <div className="auth-form-section">
        <div className="auth-form-card">
          <p className="auth-eyebrow">
            Join GutFriendly
          </p>

          <h2>Create your account</h2>

          <p className="auth-subtitle">
            It only takes a minute.
          </p>

          {message && (
            <div className="success-message">
              {message}
            </div>
          )}

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <form
            className="auth-form"
            onSubmit={handleSubmit}
          >
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="fname">
                  First name
                </label>

                <input
                  id="fname"
                  name="fname"
                  type="text"
                  value={formData.fname}
                  onChange={handleChange}
                  placeholder="Aarav"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="lname">
                  Last name
                </label>

                <input
                  id="lname"
                  name="lname"
                  type="text"
                  value={formData.lname}
                  onChange={handleChange}
                  placeholder="Sharma"
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="phoneNo">
                Phone number
              </label>

              <input
                id="phoneNo"
                name="phoneNo"
                type="tel"
                value={formData.phoneNo}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="9876501234"
                inputMode="numeric"
                autoComplete="tel"
                maxLength={14}
                required
              />

              {touched.phoneNo && !phoneCheck.isValid && (
                <p style={FIELD_ERROR_STYLE}>{phoneCheck.message}</p>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="email">
                Email address
              </label>

              <input
                id="email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="you@example.com"
                autoComplete="email"
                required
              />

              {touched.email && !emailCheck.isValid && (
                <p style={FIELD_ERROR_STYLE}>{emailCheck.message}</p>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="password">
                Password
              </label>

              <input
                id="password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                onBlur={handleBlur}
                placeholder="Create a password"
                autoComplete="new-password"
                required
              />

              <PasswordStrengthMeter evaluation={passwordCheck} />
            </div>

            <button
              className="auth-submit-button"
              type="submit"
              disabled={loading || !canSubmit}
            >
              {loading
                ? "Creating account..."
                : "Create account"}
            </button>
          </form>

          <p className="auth-switch-text">
            Already have an account?{" "}
            <Link to="/login">Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;