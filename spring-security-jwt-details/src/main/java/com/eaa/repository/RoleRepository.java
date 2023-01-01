package com.eaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eaa.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Role findByDescription (String role);

}
