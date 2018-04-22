package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sun.xml.internal.bind.v2.model.core.ID;

@Repository
public interface StudentRepostitory extends CrudRepository<student, Integer> {

	public List<student> findByEmail(String m);
	public List<student> findAll();
	Optional<student> findById(Integer i);

}
