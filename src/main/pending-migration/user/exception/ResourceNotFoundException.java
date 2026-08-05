package com.gutfriendly.app.user.exception;

public class ResourceNotFoundException extends RuntimeException {
	
//will be used for:-
//	User not found
//	Shop not found
//	Food item not found
//	Cart not found
//	Order not found
//	Review not found
//	Address not found
	
// this will return 404 error	
	

	public ResourceNotFoundException(String message) {
		super(message);
	}


}



