import { ArrowLeft } from "lucide-react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import GutFriendlyLogo from "@shared/GutFriendlyLogo";
import { registerUser } from "../services/authService";

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
    setLoading(true);

    const registrationData = {
      ...formData,
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
                placeholder="9876501234"
                required
              />
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
                placeholder="you@example.com"
                required
              />
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
                placeholder="Create a password"
                required
              />
            </div>

            <button
              className="auth-submit-button"
              type="submit"
              disabled={loading}
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