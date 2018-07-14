package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NotificationsController {
	@Autowired
	private FriendsRepository friendRepo;
	
	@Autowired
	private PostRepository post;
	
	@Autowired
	private CommentRepository commentrepo;
	
	/*@Autowired
	private NotificationsRepository notirepo;*/
	
	@Autowired
	ServiceClass s;

@GetMapping(value="/getNotifications")
ModelAndView getNotifications(HttpServletRequest req) {
	ModelAndView mv = new ModelAndView();
	
	
	String UserID = (String) req.getSession().getAttribute("myId");
	//List<Friends> friendsresult=friendRepo.findAllByUser(UserID.toString());
	//List<Post> result = post.findAll();
	List<Friends> friendsresult =friendRepo.findAllByUser(UserID);
	
	//for friend posted
	HashMap<String, String> postNotifications = new HashMap<String, String>();
	ArrayList<Integer> friendsPost = new ArrayList<>();
	
	
	//for friends commented on their post
	HashMap<String, String> postNotificationsComments = new HashMap<String, String>();
	ArrayList<Integer> friendsPostComments = new ArrayList<>();
	
	//for friends commented on your post
	HashMap<String, String> UserPostComments = new HashMap<String, String>();
	ArrayList<Integer> PostComments = new ArrayList<>();
	ArrayList<Integer> allUserPost = new ArrayList<>();
	
	List<Post> cm2 = post.findAllByuserID(UserID);//user id
	for (int j = 0; j < cm2.size(); j++) {
		allUserPost.add(cm2.get(j).getId());
	}
	System.out.println(allUserPost+"####");
	
	for (int i = 0; i < friendsresult.size(); i++) {
		// System.out.println(result.get(i).getId());
		List<Comments> cm = commentrepo.findAllByUserid(friendsresult.get(i).getFriendid());//friendid
		
		
		
		
		for (int j = 0; j < cm.size(); j++) {
			System.out.println("$$$$$"+cm.get(j).getPostid());
			if(allUserPost.contains(cm.get(j).getPostid())) {
			PostComments.add(cm.get(j).getPostid());
			System.out.println("yees");
			}
		}
		
		if(PostComments.size()>0)
		{	System.out.println("abcdef"+PostComments);
		  UserPostComments.put(friendsresult.get(i).getFriendname(), PostComments.toString().replace("[", "").replace("]", ""));
		  PostComments.clear();
	
		}
	}
	
	
	for (int i = 0; i < friendsresult.size(); i++) {
		// System.out.println(result.get(i).getId());
		List<Comments> findFriendPostComments = commentrepo.findAllByUserid(friendsresult.get(i).getFriendid());
		for (int j = 0; j < findFriendPostComments.size(); j++) {
			if(!(friendsPostComments.contains(findFriendPostComments.get(j).getPostid())))
			friendsPostComments.add(findFriendPostComments.get(j).getPostid());

		}
		
		if(friendsPostComments.size()>0)
		{	
			postNotificationsComments.put(friendsresult.get(i).getFriendname(), friendsPostComments.toString().replace("[", "").replace("]", ""));
			friendsPostComments.clear();
	
		}
	}
	
	
	
	
	
	for (int i = 0; i < friendsresult.size(); i++) {
		// System.out.println(result.get(i).getId());
		List<Post> findFriendPost = post.findAllByuserID(friendsresult.get(i).getFriendid());
		for (int j = 0; j < findFriendPost.size(); j++) {
			if(!(friendsPost.contains(findFriendPost.get(j).getId())))
			friendsPost.add(findFriendPost.get(j).getId());

		}
		
		if(friendsPost.size()>0)
		{
		postNotifications.put(friendsresult.get(i).getFriendname(),friendsPost.toString().replace("[", "").replace("]", ""));
		friendsPost.clear();
	
		}
	}
	
	
	
	
	

	mv.addObject("notificationPost",postNotifications);
	mv.addObject("postNotificationsComments",postNotificationsComments);
	mv.addObject("UserPostComments",UserPostComments);
	mv.setViewName("User_Profile");
	return mv;
	
}

}
