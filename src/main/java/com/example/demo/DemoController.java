package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.StudentRepostitory;
import com.example.demo.student;

@Controller
public class DemoController {
	
	@Autowired
	private StudentRepostitory studentrepo;
	
	
	@GetMapping(value="/a")
	public ModelAndView renderIndex() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index2");
		return mv;
	}
	
	@PostMapping(value="/student/add")
	public ModelAndView saveStudent(@RequestParam(name="name",required=true) String name,
			@RequestParam String email) {
		student n = new student();
		n.setName(name);
		n.setEmail(email);
		studentrepo.save(n);
		return new ModelAndView("redirect:/students");
		
		
	}
	@GetMapping(value="/students")  //get all the students
	public ModelAndView getAllStudents() {
		ModelAndView mv = new ModelAndView();
		List<student> students=studentrepo.findAll();
		mv.addObject("students",students);
		mv.setViewName("allStudents");
		return mv;
	}
	
	@GetMapping(value="/student")  //get one students
	public ModelAndView getoneStudent(@RequestParam(name ="id", required=true) String id) {   //get one student
		ModelAndView mv = new ModelAndView();
		try {
			Optional<student> result = studentrepo.findById(Integer.parseInt(id));//optional-since may not be present
			if(result.isPresent()) {
				student s= result.get();
				mv.addObject("student",s);
				mv.setViewName("StudentInfo");           //jsp page
			}
			else {
				
				throw new Exception("lo91");
				
			}

		}
		catch(Exception e){
			mv.addObject("error","Student not present");
			mv.setViewName("StudentError");
			e.printStackTrace();
		}
		
		return mv;
		
		//faceook
		
		
		}
		
	
	@GetMapping(value="/facebook")
	public ModelAndView renderFB() {
	
		ModelAndView mv = new ModelAndView();
		mv.setViewName("facebook");
		return mv;

}

	@PostMapping(value="/facebookRedirect")
	public ModelAndView HandleRedirect(@RequestParam(name="myId") String myId,
			@RequestParam(name="myName") String myName,@RequestParam(name="myFriends") String myFriends,
			@RequestParam(name="myEmail") String myEmail,
			 HttpServletRequest req)
	{
		System.out.println(myId+""+myEmail+""+myName+""+myFriends);
		String[] split=myFriends.split("/");
		for(int i=0;i<split.length;i++) {
			System.out.println(i+":"+split[i]);
			
		}
		//save the users
		//save the relations
		return new ModelAndView("index2");
}
}