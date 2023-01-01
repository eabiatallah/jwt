package com.eaa.request;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eaa.entity.Role;
import com.eaa.validator.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@ValidPassword
public class UserDTO implements Serializable, UserDetails {

	@NotBlank
	private String username;

	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid Email Address")
	private String email;

	@Size(min=4, max=60)
	@NotBlank(message = "New Password is mandatory")
	private String password;
	
	@Size(min=4, max=60)
	@NotBlank(message = "Confirm Password is mandatory")
	private String confirmPassword;
	
//	private List<Role> roles;
	
	private boolean enabled = false; // default value

	private Collection<? extends GrantedAuthority> authorities;

	public UserDTO(String username, String email, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled) {

		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.enabled = enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}
 
}
