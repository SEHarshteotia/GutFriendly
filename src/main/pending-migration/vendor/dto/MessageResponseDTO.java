package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple success message response.
 * <p>
 * Used by password change and menu item delete endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

	private String message;
}
