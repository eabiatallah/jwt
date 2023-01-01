package com.eaa.response;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	// private static final long serialVersionUID = -8091879091924046844L;
	private String jwttoken;

	public JwtResponse() {
		super();
	}

	public JwtResponse(String jwttoken) {
		super();
		this.jwttoken = jwttoken;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}

}
