package com.example.demo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer>{
	Optional<Admin> findByAdminID (String id);
	}
