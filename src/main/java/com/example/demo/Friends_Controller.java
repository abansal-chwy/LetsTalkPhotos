package com.example.demo;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class Friends_Controller {
	@Autowired
	private UserProfileRepository userprofile;
	
	@Autowired
	private PostRepository post;
	
	@Autowired
	ServiceClass s;
	
	@Autowired
	CommentRepository cr;
	
	@GetMapping(value="/FriendProfile")
	public ModelAndView openFriendProfile(@RequestParam(name = "friendid") String friendid,
			HttpServletRequest req) {
		
		
		req.getSession().setAttribute("friendid", friendid);
		return new ModelAndView("redirect:/viewFriendProfile");
		
	}
	@GetMapping(value="/viewFriendProfile")
	public ModelAndView viewFriend(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		String id=(String) req.getSession().getAttribute("friendid");
		Optional<UserProfile> result = userprofile.findByUserID(id.toString());
		UserProfile u = result.get();
		String ImgSrc="http://"+"softwareengineeringdemo1"+".s3.amazonaws.com/"+u.getImage();
		HashMap<Integer, String> comments = s.getAllCommentsFriend(req);

		List<Post> resultUserPost = post.findAllByuserID(id);
		HashMap<String, String> UserPostComments=s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments=s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications=s.friendPost(req);
		mv.addObject("postNotificationsComments",postNotificationsComments);
		mv.addObject("UserPostComments",UserPostComments);
		mv.addObject("notificationPost",postNotifications);
		mv.addObject("ImgSrc",ImgSrc);
		mv.addObject("user", u);
		mv.addObject("posts", resultUserPost);
		mv.addObject("findcomments", comments);
		mv.setViewName("Friend_Profile");
		return mv;
		
	}
	@PostMapping(value="/addCommentFriend")
	public ModelAndView addComment(@RequestParam("postid") int postid,
			@RequestParam("comment") String comment,HttpServletRequest req) {
	   ModelAndView mv = new ModelAndView();
		System.out.println(postid+"*************abcuhidue");
		//System.out.println("comment"+comment);
		
		String username =(String) req.getSession().getAttribute("name");
		Comments c = new Comments();
		c.setUsername(username);
		c.setPostid(postid);
		c.setComment(comment.toString());
		cr.save(c);
		return new ModelAndView("redirect:/viewFriendProfile"); 
}
	
}

