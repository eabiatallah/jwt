package com.eaa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.eaa.request.UserDTO;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, UserDTO> {

	@Override
	public boolean isValid(UserDTO user, ConstraintValidatorContext context) {
		String password = user.getPassword();
		String confirmPassword = user.getConfirmPassword();

		if (password == null || !password.equals(confirmPassword)) {
			return false;
		}

		return true;
	}
}