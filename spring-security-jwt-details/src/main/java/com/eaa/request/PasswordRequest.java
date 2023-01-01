package com.eaa.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordRequest {
	
	private String email;
	private String oldPassword;
	private String newPassword;
	

}
