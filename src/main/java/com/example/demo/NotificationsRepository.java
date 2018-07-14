package com.example.demo;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository  extends CrudRepository<Notifications, Integer> {

	/*public List<Notifications> findAllByuserID(String userID);
	*/
}
