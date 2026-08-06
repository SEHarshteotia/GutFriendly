import { ArrowLeft } from "lucide-react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import GutFriendlyLogo from "@shared/GutFriendlyLogo";
import { loginUser } from "../services/authService";

function LoginPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    phoneNo: "",
    password: "",
  });

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

    setError("");
    setLoading(true);

    try {
      const response = await loginUser(formData);

      localStorage.setItem(
        "userId",
        String(response.userId)
      );

      localStorage.setItem(
        "userName",
        response.fname
      );

      localStorage.setItem(
        "rewardPoints",
        String(response.rewardPoints)
      );

      navigate("/home");
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

          <h1>Welcome back to safer food choices.</h1>

          <p>
            Sign in to access saved shops, orders, reviews,
            rewards and your personalised GutFriendly
            experience.
          </p>
        </div>
      </div>

      <div className="auth-form-section">
        <div className="auth-form-card">
          <p className="auth-eyebrow">
            Welcome back
          </p>

          <h2>Sign in to GutFriendly</h2>

          <p className="auth-subtitle">
            Use your registered phone number.
          </p>

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <form
            className="auth-form"
            onSubmit={handleSubmit}
          >
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
              <label htmlFor="password">
                Password
              </label>

              <input
                id="password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter your password"
                required
              />
            </div>

            <button
              className="auth-submit-button"
              type="submit"
              disabled={loading}
            >
              {loading
                ? "Signing in..."
                : "Sign in"}
            </button>
          </form>

          <p className="auth-switch-text">
            New to GutFriendly?{" "}
            <Link to="/register">
              Create an account
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;