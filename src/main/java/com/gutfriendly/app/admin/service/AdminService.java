package com.gutfriendly.app.admin.service;

import com.gutfriendly.app.admin.dto.request.AdminRegisterRequest;
import com.gutfriendly.app.admin.model.AdminDetails;

public interface AdminService {
	//Saves Admin Registration details in database 
	public AdminDetails saveAdmin(AdminRegisterRequest request);
	
	
	

}
