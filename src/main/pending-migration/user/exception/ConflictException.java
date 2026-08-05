package com.gutfriendly.app.user.exception;

public class ConflictException extends RuntimeException {

// occurs when:-
		
//	Shop already present in wishlist
//	Review already submitted
//	Cart contains food from another shop
//	Order cannot be cancelled
	
//	will give 409 conflict	

    public ConflictException(String message) {
        super(message);
    }
}