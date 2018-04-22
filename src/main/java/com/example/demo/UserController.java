package com.example.demo;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.User;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private FriendsRepository friendRepo;
	
	@GetMapping(value="/")
	public ModelAndView renderIndex() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Login_user");
		return mv;
	}
	
	//First check if user is not present 
	
	
	@PostMapping(value="/addUser")
	public ModelAndView HandleRedirect(@RequestParam(name="myId") String myId,
			@RequestParam(name="myName") String myName,@RequestParam(name="myFriends") String myFriends,
			@RequestParam(name="myEmail") String myEmail,HttpServletRequest req)
	{
		req.getSession().setAttribute("myId", myId);
		req.getSession().setAttribute("name",myName);
		req.getSession().setAttribute("friends",myFriends);
		System.out.println(myId+""+myEmail+""+myName+""+myFriends);
		String[] split=myFriends.split("/");
		
		for(int i=0;i<split.length;i++) {
			System.out.println(i+":"+split[i]);
			
		}
		
		
			Optional<User> result = userRepo.findById(myId.toString());
			if(!result.isPresent()) {
				
				System.out.println("in here");
				User n = new User();
				n.setId(myId);
				n.setName(myName);
				n.setEmail(myEmail);
				
				userRepo.save(n);
				
				if(!(split.length==0)) {
				for(int i=0;i<split.length;i++) {
					
					Friends f=new Friends();
					f.setUser(myId);
					f.setFriendid(split[i]);
					f.setFriendname(split[i+1]);
					friendRepo.save(f);
					i++;
							
					
				}
				}
				
				
			}
			

		
		
		
		
		
			
		//save the users
		//save the relations
		return new ModelAndView("redirect:/VerifyProfile");
}
	
	
	
	
	
	
//	@GetMapping(value="/userProfile")  //get one students
//	public ModelAndView getUserProfile() {
//		
//		ModelAndView mv = new ModelAndView();
//		<user> result = userRepo.userRepo.findById(myId.toString())
//	}
	
}
