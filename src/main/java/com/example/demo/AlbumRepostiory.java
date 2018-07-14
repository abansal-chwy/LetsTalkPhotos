package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepostiory extends CrudRepository<Album, Integer>{
	
public List<Album> findAllByUserid(String userid);
public Optional<Album> findById(Integer i);
}
