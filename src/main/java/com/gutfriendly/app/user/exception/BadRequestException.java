package com.gutfriendly.app.user.exception;

public class BadRequestException extends RuntimeException {

//	This is used for invalid request:-
//		Quantity must be greater than zero
//		Rating must be between 1 and 5
//		Delivery address is required
//		Payment method is required
//		Search keyword cannot be empty
//		Cart is empty
//		Food item is unavailable
	
//		will return 404 error
	
    public BadRequestException(String message) {
        super(message);
    }
}