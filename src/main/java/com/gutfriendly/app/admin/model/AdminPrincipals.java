package com.gutfriendly.app.admin.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminPrincipals implements UserDetails {
	
	private AdminDetails admin;
	
	public AdminPrincipals (AdminDetails admin) {
		this.admin= admin;
		
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of();
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return admin.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return admin.getEmail();
	}
	@Override
	public boolean isAccountNonExpired(){
	    return true;
	}

	@Override
	public boolean isAccountNonLocked(){
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired(){
	    return true;
	}

	@Override
	public boolean isEnabled(){
	    return admin.isActiveStatus();
	}

}
