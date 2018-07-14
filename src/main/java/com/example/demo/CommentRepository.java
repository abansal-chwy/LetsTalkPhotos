package com.example.demo;

import java.util.List;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comments, Integer> {
	public List<Comments> findAllBypostid(int id);
	
	public List<Comments> findAllByUserid(String id);
	public List<Comments> findAll();
}
