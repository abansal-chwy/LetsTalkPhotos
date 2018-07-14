package com.example.demo;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import com.example.demo.User;

@Controller
public class AdminController {
	@Autowired
	private AdminRepository ad;

	@Autowired
	private UserRepository user;
	
	@Autowired
	private UserProfileRepository userprofile;
	
	
	@Autowired
	private PostRepository post;
	
	@Autowired
	ServiceClass s;
	
	@Autowired
	CommentRepository cr;
	
	@Autowired
	PostAudioRepository audiorepo;
	
	
	@GetMapping(value="/AdminLogin")
	public ModelAndView veiwAdminProfile(HttpServletRequest req) {
	
		ModelAndView retrieve = new ModelAndView();
		
		
		String UserID = (String) req.getSession().getAttribute("myId");
		Optional<UserProfile> result = userprofile.findByUserID(UserID.toString());// optional-since may not be present
		UserProfile u = result.get();
	
		String ImgSrc="http://"+"softwareengineeringdemo1"+".s3.amazonaws.com/"+u.getImage();
		req.getSession().setAttribute("ImgSrc", ImgSrc);
		
		//HashMap<Integer, String> comments = s.getAllComments(req,postid);
		HashMap<String, String> UserPostComments=s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments=s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications=s.friendPost(req);
		//call service class to get all the post
		
		List resultUserPost =s.getUserPost(req);
		retrieve.addObject("ImgSrc",ImgSrc);
		retrieve.addObject("user", u);
		retrieve.addObject("posts", resultUserPost);
		//retrieve.addObject("findcomments", comments);
		retrieve.addObject("UserPostComments",UserPostComments);
		retrieve.addObject("notificationPost",postNotifications);
		retrieve.addObject("postNotificationsComments",postNotificationsComments);
		
		
		retrieve.setViewName("AdminProfile");
		return retrieve;
	}
	
	
	
	@GetMapping(value="/getUsers")
	public ModelAndView veiwAllUsers(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		List<User> result=user.findAll();
		mv.addObject("user",result);
		System.out.println("result is "+result);
		mv.setViewName("AdminViewUsers");
		return mv;
	}
	
	
	@GetMapping(value="/userProfile")
	public ModelAndView openFriendProfile(@RequestParam(name = "userid") String userid,
			HttpServletRequest req) {
		
		
		req.getSession().setAttribute("userid", userid);
		return new ModelAndView("redirect:/viewUserProfile");
		
	}
	
	
	@GetMapping(value="/viewUserProfile")
	public ModelAndView viewFriend(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		String userid=(String) req.getSession().getAttribute("userid");
		Optional<UserProfile> result = userprofile.findByUserID(userid.toString());
		UserProfile u = result.get();
		String ImgSrc="http://"+"softwareengineeringdemo1"+".s3.amazonaws.com/"+u.getImage();
		//HashMap<Integer, String> comments = s.getAllCommentsFriend(req);

		List<Post> resultUserPost = post.findAllByuserID(userid);
		System.out.println("id is"+userid);
		mv.addObject("ImgSrc",ImgSrc);
		mv.addObject("user", u);
		mv.addObject("posts", resultUserPost);
		//mv.addObject("findcomments", comments);
		mv.setViewName("Admin_User_Profile");
		return mv;
		
	}
	
	
	@PostMapping(value ="/deletePost")
	public ModelAndView deletePost(@RequestParam(name = "postid") int postid,HttpSession session ) {
		Optional<Post> getpost = post.findById(postid);
	   
		System.out.println("posidddd"+postid);
		Post p = new Post();
	    post.delete(postid);
	    /*List<Comments> comments = cr.findAllBypostid(postid);
	    if(!(comments.isEmpty())) {
	    	cr.delete(postid);
	    }
	    Optional<PostAudio> audio=audiorepo.findBypostid(postid);
	    if(!(audio.isPresent())) {
	    audiorepo.delete(postid);
	    }*/
	    return  new ModelAndView("redirect:/viewUserProfile");
}
	
	
	
}