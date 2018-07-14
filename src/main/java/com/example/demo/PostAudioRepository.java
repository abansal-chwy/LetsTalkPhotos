package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PostAudioRepository extends CrudRepository<PostAudio, Integer> {
	public List<PostAudio> findAllBypostid(int id);
	Optional<PostAudio> findBypostid(Integer i);
	Optional<PostAudio> findBytext(String i);

}
