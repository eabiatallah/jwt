package com.eaa.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eaa.config.JwtTokenUtil;
import com.eaa.request.JwtRequest;
import com.eaa.request.PasswordRequest;
import com.eaa.request.UserDTO;
import com.eaa.request.UserRoleRequest;
import com.eaa.response.JwtResponse;
import com.eaa.response.UserResponse;
import com.eaa.services.JwtUserDetailsService;

import io.jsonwebtoken.impl.DefaultClaims;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserDTO userDto, HttpServletRequest request) throws Exception {
		return ResponseEntity.ok().body(userDetailsService.registerUser(userDto, request));
	}
	
	@GetMapping("/verifyRegistration")
	public ResponseEntity<String> verifyRegistration(@RequestParam("verificationCode") String verificationCode) throws Exception {
		return ResponseEntity.ok().body(userDetailsService.verifyRegistration(verificationCode));
	}
	
	@PostMapping("/role/addToUser")
	public ResponseEntity<?> addRoleToUser(@RequestBody UserRoleRequest request) {

		userDetailsService.addRoleToUser(request.getEmail(), request.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/changeUserPassword")
	public ResponseEntity<String> changeUserPassword(@RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
		return ResponseEntity.ok().body(userDetailsService.changeUserPassword(passwordRequest));
	}

	@PostMapping("/authenticate")
	public String createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		try {
			UserDTO userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			// org.springframework.security.core.userdetails.User [Username=ea, Password=[PROTECTED],...]
			final String token = jwtTokenUtil.generateToken(userDetails);
			return token;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			System.out.println(" key = "+entry.getKey()+" Value = "+entry.getValue() );
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}



}
