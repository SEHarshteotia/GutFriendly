package com.gutfriendly.app.admin.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.model.AdminDetails;
import com.gutfriendly.app.admin.model.AdminPrincipals;
import com.gutfriendly.app.admin.repository.AdminDetailsRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	final AdminDetailsRepository ar;

	MyUserDetailsService(AdminDetailsRepository ar) {
		this.ar = ar;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AdminDetails> admin = ar.findByEmail(username);
		if (admin.isEmpty()) {
			System.out.print("User 404");
			throw new UsernameNotFoundException("User 404");
		}

		return new AdminPrincipals(admin.get());
	}

}
