package com.example.demo;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CommentController {

	@Autowired
	ServiceClass s;
	
	@Autowired
	private CommentRepository cr;
	
   @PostMapping(value="/addComment")
	public ModelAndView addComment(@RequestParam("postid") int postid,
			@RequestParam("comment") String comment,HttpServletRequest req) {
	   ModelAndView mv = new ModelAndView();
		System.out.println(postid+"*************abcuhidue");
		//System.out.println("comment"+comment);
		
		String username =(String) req.getSession().getAttribute("name");
		String userid=(String) req.getSession().getAttribute("myId");
		
		Comments c = new Comments();
		c.setUsername(username);
		c.setPostid(postid);
		c.setComment(comment.toString());
		
		c.setUserid(userid);
		cr.save(c);
		return new ModelAndView("redirect:/RetreivePost"); 
		
		
		
	}
	
 
   @PostMapping(value="/addCommentfriend")
	public ModelAndView addCommentfriend(@RequestParam("postid") String postid,
			@RequestParam("comment") String comment,HttpServletRequest req) {
	   ModelAndView mv = new ModelAndView();
		System.out.println(postid+"*************abcuhidue");
		//System.out.println("comment"+comment);
		
		String username =(String) req.getSession().getAttribute("name");
		String userid=(String) req.getSession().getAttribute("myId");
		
		System.out.println("&&&&&&&&"+postid);
		Comments c = new Comments();
		c.setUsername(username);
		c.setPostid(Integer.parseInt(postid));
		c.setComment(comment.toString());
		
		c.setUserid(userid);
		cr.save(c);
		return new ModelAndView("redirect:/viewFriendProfile"); 
		
		
		
	}
	
   
   
}
