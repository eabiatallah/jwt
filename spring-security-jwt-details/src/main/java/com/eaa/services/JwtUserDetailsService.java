package com.eaa.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eaa.entity.Role;
import com.eaa.entity.User;
import com.eaa.repository.RoleRepository;
import com.eaa.repository.UserRepository;
import com.eaa.request.PasswordRequest;
import com.eaa.request.UserDTO;
import com.eaa.response.UserResponse;
import com.eaa.utils.ServiceUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername\n\n\n\n ----");
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		try {
			User user = userRepository.findByUsername(username);
			if (user != null) {
				if (!user.isEnabled()) {
					throw new DisabledException("disabled username : " + user.getUsername());
				}
				user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getDescription())));
				// return new User(user.getUsername(), user.getPassword(),
				// roles);
				return new UserDTO(user.getUsername(), user.getEmail(), user.getPassword(), authorities,
						user.isEnabled());
			} else
				throw new UsernameNotFoundException("User not found with username: " + username);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	public String verifyRegistration(String code) {
		try {
			User user = userRepository.findByVerificationCode(code);
			Calendar cal = Calendar.getInstance();
			if (user == null) {
				return "Invalid Code";
			} else if (user.isEnabled()) {
				return "Already Registered";
			} else if (user.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
				return "Code Expired, Please reset it !";
			} else {
				user.setEnabled(true);
				userRepository.save(user);
				return "User Verified Successfully";
			}
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	public String changeUserPassword(PasswordRequest passwordRequest) {
		try {
			User user = userRepository.findByEmail(passwordRequest.getEmail());
			if (!checkIfValidOldPassword(user, passwordRequest.getOldPassword())) {
				return "Invalid Old Password";
			} else {
				user.setPassword(passwordRequest.getNewPassword());
				userRepository.save(user);
				return "Password Changed Successfully";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public UserResponse registerUser(UserDTO userDto, HttpServletRequest request) {
		UserResponse response = new UserResponse();
		try {
			User user = userRepository.findByEmail(userDto.getEmail());
			if (user == null) {
				String verificationCode = UUID.randomUUID().toString();
				User newUser = populateUserData(userDto, verificationCode);
				userRepository.save(newUser);
				response.setMessage(verificationEmail(verificationCode, ServiceUtils.applicationUrl(request)));
				response.setStatus("OK");
			} else {
				response.setMessage("User Already Exists");
				response.setStatus("Ok");
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("Server Error");
		}
		return response;
	}
	
	private User populateUserData(UserDTO userDto, String verificationCode) throws Exception {
		User user = new User(userDto.getUsername(), userDto.getEmail(), 
				             bcryptEncoder.encode(userDto.getPassword()),
				             verificationCode);
		return user;
	}
	

	private String verificationEmail(String verificationCode, String applicationUrl) {
		String url = applicationUrl + "/verifyRegistration?verificationCode="+verificationCode;
		log.info("Click link to enable your account: {}", url);
		return url;
	}
	
	public boolean checkIfValidOldPassword(User user, String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

	public void addRoleToUser(String email, String roleName) {
		User user = userRepository.findByEmail(email);
		Role role = roleRepository.findByDescription(roleName);
		user.getRoles().add(role);
		log.info("Adding role {} to user {}", role.getDescription(), user.getEmail());
		userRepository.save(user);
	}

}
