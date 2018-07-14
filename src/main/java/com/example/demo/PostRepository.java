package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface PostRepository extends CrudRepository<Post, Integer> {

	public Optional<Post> findByuserID(String userID);
	
/*	@Query("select p.userID, p.id,group_concat(c.comment) as comments from Post p left join Comments c on (p.id=c.postid)\r\n" + 
			"group by id")*/
	public List<Post> findAllByuserID(String userID);
	public List<Post> findAll();
	Optional<Post> findById(Integer i);
	Optional<Post> findByImage(String i);

}
