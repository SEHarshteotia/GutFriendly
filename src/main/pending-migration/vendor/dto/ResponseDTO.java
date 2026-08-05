package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Generic message and status response payload.
 */
@Data
public class ResponseDTO {
	String message;
	String status;
}
