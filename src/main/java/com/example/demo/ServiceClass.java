package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceClass {

	@Autowired
	private UserProfileRepository userprofile;
	

	@Autowired
	private PostRepository post;
	
	@Autowired
	private CommentRepository cr;

	@Autowired
	private FriendsRepository friendRepo;
		
	@Autowired
	private CommentRepository commentrepo;
	
	public UserProfile getUserProfile(HttpServletRequest req) {
		
		
		
		String UserID = (String) req.getSession().getAttribute("myId");
		Optional<UserProfile> result = userprofile.findByUserID(UserID.toString());// optional-since may not be present
		UserProfile u = result.get();
		return u;
		
	}
	
	public List<Post> getUserPost(HttpServletRequest req) {

		String UserID = (String) req.getSession().getAttribute("myId");
		List<Post> result = post.findAllByuserID(UserID);
		return result;
		
		
}
	//to view user post comment after clicking photo
	public Optional<Post> getUserPostFriend(HttpServletRequest req, String postid) {

		
		Optional<Post> result = post.findById(Integer.parseInt(postid));
		return result;
		
		
}
	
	
	public HashMap<Integer, String> getAllComments(HttpServletRequest req,String postid) {
		
		String UserID = (String) req.getSession().getAttribute("myId");
		List<Post> result = post.findAllByuserID(UserID);
		HashMap<Integer, String> comments = new HashMap<Integer, String>();
		ArrayList<String> addAllComents = new ArrayList<>();

		
			// System.out.println(result.get(i).getId());
			List<Comments> findcomments = cr.findAllBypostid(Integer.parseInt(postid));
			for (int j = 0; j < findcomments.size(); j++) {
				addAllComents.add(findcomments.get(j).getUsername() + "-" + findcomments.get(j).getComment());
			}
			comments.put(Integer.parseInt(postid), addAllComents.toString().replace("[", "").replace("]", ""));
			
			addAllComents.clear();
		
		return comments;
}
public HashMap<Integer, String> getAllCommentsFriend(HttpServletRequest req) {
		
	String id=(String) req.getSession().getAttribute("friendid");
		List<Post> result = post.findAll();
		HashMap<Integer, String> comments = new HashMap<Integer, String>();
		ArrayList<String> addAllComents = new ArrayList<>();

		for (int i = 0; i < result.size(); i++) {
			// System.out.println(result.get(i).getId());
			List<Comments> findcomments = cr.findAllBypostid(result.get(i).getId());
			for (int j = 0; j < findcomments.size(); j++) {
				addAllComents.add(findcomments.get(j).getUsername() + "-" + findcomments.get(j).getComment());

			}

			comments.put(result.get(i).getId(), addAllComents.toString().replace("[", "").replace("]", ""));
			addAllComents.clear();
		}
		return comments;
	
}

public HashMap<String, String> friendCommentOnUserPost(HttpServletRequest req) {
	String UserID = (String) req.getSession().getAttribute("myId");
	List<Friends> friendsresult =friendRepo.findAllByUser(UserID);
	
	
	//for friends commented on your post
	HashMap<String, String> UserPostComments = new HashMap<String, String>();
	ArrayList<Integer> PostComments = new ArrayList<>();
	ArrayList<Integer> allUserPost = new ArrayList<>();
	
	List<Post> cm2 = post.findAllByuserID(UserID);//user id
	for (int j = 0; j < cm2.size(); j++) {
		allUserPost.add(cm2.get(j).getId());
	}
	
	
	for (int i = 0; i < friendsresult.size(); i++) {
		// System.out.println(result.get(i).getId());
		List<Comments> cm = commentrepo.findAllByUserid(friendsresult.get(i).getFriendid());//friendid
		
		
		
		
		for (int j = 0; j < cm.size(); j++) {
			System.out.println("$$$$$"+cm.get(j).getPostid());
			if(allUserPost.contains(cm.get(j).getPostid())) {
			PostComments.add(cm.get(j).getPostid());
			
			}
		}
		
		if(PostComments.size()>0)
		{	System.out.println("abcdef"+PostComments);
		  UserPostComments.put(friendsresult.get(i).getFriendname(), PostComments.toString().replace("[", "").replace("]", ""));
		  PostComments.clear();
	
		}
	}
	
	
	
	
	
		return UserPostComments;

}
public HashMap<String, String> friendCommentOnHisPost(HttpServletRequest req) {
	String UserID = (String) req.getSession().getAttribute("myId");
	//List<Friends> friendsresult=friendRepo.findAllByUser(UserID.toString());
	//List<Post> result = post.findAll();
	List<Friends> friendsresult =friendRepo.findAllByUser(UserID);
	HashMap<String, String> postNotificationsComments = new HashMap<String, String>();
	
	for (int i = 0; i < friendsresult.size(); i++) {
		// System.out.println(result.get(i).getId());
		List<Comments> findFriendPostComments = commentrepo.findAllByUserid(friendsresult.get(i).getFriendid());
		for (int j = 0; j < findFriendPostComments.size(); j++) {
			
			postNotificationsComments.put(findFriendPostComments.get(j).getPostid().toString(),friendsresult.get(i).getFriendname());
			
	
		
	}
		
	}
	return postNotificationsComments;
	
	
	
}


public HashMap<String, String> friendPost(HttpServletRequest req) {
	String UserID = (String) req.getSession().getAttribute("myId");
	
	List<Friends> friendsresult =friendRepo.findAllByUser(UserID);
	HashMap<String, String> postNotifications = new HashMap<String, String>();

	

	for (int i = 0; i < friendsresult.size(); i++) {
		// System.out.println(result.get(i).getId());
		List<Post> findFriendPost = post.findAllByuserID(friendsresult.get(i).getFriendid());
		for (int j = 0; j < findFriendPost.size(); j++) {
			
		postNotifications.put(findFriendPost.get(j).getId().toString(),friendsresult.get(i).getFriendname());
	
	
		}
	}
	return postNotifications;
}

}