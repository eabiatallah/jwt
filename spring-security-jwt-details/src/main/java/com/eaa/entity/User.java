package com.eaa.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.eaa.constants.*;
import com.eaa.utils.ServiceUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ref_user", uniqueConstraints = @UniqueConstraint(columnNames = "email_address"))
@Data
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String username;

	@Column(name = "email_address", nullable = false)
	private String email;
	
	@Column(length = 60)
	private String password;
	
	
	private boolean enabled = false;
	
	private String verificationCode;

	private Date expirationTime;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
	           joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id" ),
	           inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<>();
	
//	public void addRole(Role role) {
//		this.roles.add(role);
//	}
//	
	
	public User(String username, String email, String password,  String verificationCode) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.verificationCode = verificationCode;
		this.expirationTime = ServiceUtils.calculateExpirationDate(Constants.EXPIRATION_TIME);
	}
	
	
}
