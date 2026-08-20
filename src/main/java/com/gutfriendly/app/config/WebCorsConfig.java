package com.gutfriendly.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS rules for browser callers.
 *
 * <p>In production the frontend reaches the API through Vercel rewrites, so
 * those requests are same-origin and never consult this policy. It still
 * matters for local development and for anything calling the Render URL
 * directly, which is why the origin list is configurable instead of a
 * hardcoded set of localhost ports.
 *
 * <p>Override with the {@code APP_CORS_ALLOWED_ORIGINS} environment variable
 * (comma separated). Wildcards are intentionally not used: credentials are
 * allowed, and the two cannot be combined.
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

	/** Must stay a compile-time constant to be usable inside the annotation. */
	private static final String DEFAULT_ORIGINS =
			"http://localhost:5173,"
			+ "http://localhost:5174,"
			+ "http://localhost:5175,"
			+ "https://gutfriendly.vercel.app";

	private final String allowedOrigins;

	public WebCorsConfig(
			@Value("${app.cors.allowed-origins:" + DEFAULT_ORIGINS + "}") String allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] origins = allowedOrigins.split("\\s*,\\s*");

		registry.addMapping("/**")
				.allowedOrigins(origins)
				.allowedOriginPatterns("https://*.vercel.app")
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}
}
