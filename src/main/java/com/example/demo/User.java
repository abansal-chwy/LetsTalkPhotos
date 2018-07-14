package com.example.demo;


import javax.persistence.Column;

import javax.persistence.Entity;

import javax.persistence.Id;


@Entity
public class User {
	
	@Id
	@Column(name="user_id",unique=true)		
    private String id;

    private String name;
    private String email;
    
       
   
    
    
    
    public String getId() {
		return id;
	}


	public void setId(String myId) {
		this.id = myId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	
	
	
     
	
}
