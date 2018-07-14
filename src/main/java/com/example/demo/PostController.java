package com.example.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller

public class PostController {

	@Autowired
	private CommentRepository cr;

	@Autowired
	private PostAudioRepository audiorepo;

	@Autowired
	uploadToS3 s3;

	@Autowired
	private PostRepository post;

	@Autowired
	ServiceClass s;

	@Value("#{environment['AWS_ACCESS_KEY_ID']}")
	public String accessKey;
	@Value("#{systemEnvironment['AWS_SECRET_ACCESS_KEY']}")
	String SecretKey;

	@PostMapping(value = "/postPicture")
	public ModelAndView PostPicture(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		HashMap<String, String> UserPostComments = s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments = s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications = s.friendPost(req);
		mv.addObject("postNotificationsComments", postNotificationsComments);
		mv.addObject("UserPostComments", UserPostComments);
		mv.addObject("notificationPost", postNotifications);
		mv.setViewName("PostImage");
		return mv;
	}

	@PostMapping(value = "/saveImage")
	public ModelAndView saveImage(@RequestParam("recording") String recording, HttpServletRequest req)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		if (recording.isEmpty())
			throw new Exception("recoding data is empty");
		Decoder decoder = Base64.getDecoder();

		byte[] decodeByte = decoder.decode(recording.split(",")[1]);
		BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, SecretKey);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(cred))
				.withRegion(Regions.US_EAST_2).build();
		String UserID = (String) req.getSession().getAttribute("myId");
		String userName = (String) req.getSession().getAttribute("name");
		try {

			FileOutputStream fos;

			fos = new FileOutputStream("post.png");
			fos.write(decodeByte);
			fos.close();
			Random rand = new Random();
			int value = rand.nextInt(100000);

			PutObjectRequest putReq = new PutObjectRequest("softwareengineeringdemo1", value + "_" + UserID + ".png",
					new FileInputStream("post.png"), new ObjectMetadata())

							.withCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(putReq);

			Post p = new Post();
			String image = "http://softwareengineeringdemo1.s3.amazonaws.com/" + value + "_" + UserID + ".png";
			p.setUserID(UserID);
			p.setUsername(userName);
			p.setImage(image);
			post.save(p);
			Optional<Post> imageSaved = post.findByImage(image);
			Post getImage = imageSaved.get();
			int postid = getImage.getId();
			req.getSession().setAttribute("postid", postid);

		} catch (IOException e) {
			e.printStackTrace();
			mv.setViewName("error");
			return mv;
		}

		return new ModelAndView("redirect:/uploadAudio");

	}

	@GetMapping(value = "/RetreivePost")
	public ModelAndView getUserPost(HttpServletRequest req) {

		ModelAndView retrieve = new ModelAndView();
		
		String email="akkibansal@gmail.com";
		if(req.getSession().getAttribute("myEmail").equals(email)) {
			System.out.println("Email is equal...");
			return new ModelAndView("redirect:/AdminLogin");
		}

		String UserID = (String) req.getSession().getAttribute("myId");

		List<Post> result = post.findAllByuserID(UserID);
		List<Post> allPosts = post.findAll();
		HashMap<Integer, String> comments = new HashMap<Integer, String>();
		ArrayList<String> addAllComents = new ArrayList<>();

		for (int i = 0; i < allPosts.size(); i++) {
			// System.out.println(result.get(i).getId());
			List<Comments> findcomments = cr.findAllBypostid(allPosts.get(i).getId());
			for (int j = 0; j < findcomments.size(); j++) {
				addAllComents.add(findcomments.get(j).getUsername() + "-" + findcomments.get(j).getComment());

			}

			comments.put(allPosts.get(i).getId(), addAllComents.toString().replace("[", "").replace("]", ""));
			addAllComents.clear();
		}
		System.out.println("*****88" + comments);
		// get profile picture
		String ImgSrc = (String) req.getSession().getAttribute("ImgSrc");
		UserProfile u = s.getUserProfile(req);
		// req.getSession().setAttribute("result", result);
		HashMap<String, String> UserPostComments = s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments = s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications = s.friendPost(req);
		retrieve.addObject("postNotificationsComments", postNotificationsComments);
		retrieve.addObject("UserPostComments", UserPostComments);
		retrieve.addObject("notificationPost", postNotifications);

		retrieve.addObject("ImgSrc", ImgSrc);
		retrieve.addObject("user", u);
		retrieve.addObject("posts", result);
		retrieve.addObject("findcomments", comments);

		retrieve.setViewName("User_Profile");
		return retrieve;

	}

	@GetMapping(value = "/uploadAudio")
	public ModelAndView postAudio(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		HashMap<String, String> UserPostComments = s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments = s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications = s.friendPost(req);
		mv.addObject("postNotificationsComments", postNotificationsComments);
		mv.addObject("UserPostComments", UserPostComments);
		mv.addObject("notificationPost", postNotifications);
		mv.setViewName("Post");
		return mv;

	}

	/*
	 * ModelAndView audio = new ModelAndView(); String addr =
	 * s3.uploadAudio(image.getOriginalFilename(), image.getInputStream());
	 * audio.addObject("imgSrc", addr); audio.setViewName("User_Profile");
	 */

	@PostMapping(value = "/saveAudio")
	public ModelAndView saveAudio(@RequestParam("recording") String recording,
			@RequestParam("textannotation") String text, HttpServletRequest req) throws Exception {
		ModelAndView successPage = new ModelAndView("success");
		if (!(recording.isEmpty()))
		{		
		Decoder decoder = Base64.getDecoder();
		System.out.println(recording);
		byte[] decodeByte = decoder.decode(recording.split(",")[1]);

		BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, SecretKey);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(cred))
				.withRegion(Regions.US_EAST_2).build();
		String UserID = (String) req.getSession().getAttribute("myId");
		try {
			FileOutputStream fos;
			fos = new FileOutputStream("MyAudio.webm");
			fos.write(decodeByte);
			fos.close();

			Random rand = new Random();
			int value = rand.nextInt(100000);

			PutObjectRequest putReq = new PutObjectRequest("softwareengineeringdemo1", value + "_" + UserID + ".webm",
					new FileInputStream("MyAudio.webm"), new ObjectMetadata())

							.withCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(putReq);

			int postid = (int) req.getSession().getAttribute("postid");
			PostAudio p = new PostAudio();

			p.setAudio("http://softwareengineeringdemo1.s3.amazonaws.com/" + value + "_" + UserID + ".webm");
			p.setPostid(postid);
			p.setText(text);
			audiorepo.save(p);
		} catch (IOException e) {
			e.printStackTrace();

		}
		}
		/*
		 * String UserID = (String) req.getSession().getAttribute("myId"); String postid
		 * = "1"; String audioURI = s3.uploadAudio("myId" + "-" + postid + ".webm", new
		 * FileInputStream("MyAudio.webm")); successPage.addObject("audioURI",
		 * audioURI);
		 */
		return new ModelAndView("redirect:/RetreivePost");

	}

	
	
	
	
	@GetMapping(value = "/viewSelectedPostfriend")
	public ModelAndView viewPostfriend(@RequestParam("postid") String postid, HttpServletRequest req) {
		System.out.println("in view selected post");
		ModelAndView mv = new ModelAndView();
		Optional<Post> result = post.findById(Integer.parseInt(postid));
		Post p = result.get();
		
		String ImgSrc = p.getImage();
		
		System.out.println("post id got is " + postid);

		PostAudio pa = new PostAudio();
		
		Optional<PostAudio> resultaudio = audiorepo.findBypostid(Integer.parseInt(postid));
		
		if (resultaudio.isPresent()) {

			pa = resultaudio.get();
			String AudioSrc = pa.getAudio();
			String text = pa.getText();

			mv.addObject("AudioSrc", AudioSrc);
			if (!(text.equals(null))) {
				mv.addObject("text", text);
			}

		}

		mv.addObject("ImgSrc", ImgSrc);
		HashMap<Integer, String> comments = s.getAllComments(req,postid);
		
		HashMap<String, String> UserPostComments = s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments = s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications = s.friendPost(req);
		//Optional<Post> resultUserPost = s.getUserPostFriend(req,postid);
		mv.addObject("postNotificationsComments", postNotificationsComments);
		mv.addObject("UserPostComments", UserPostComments);
		mv.addObject("notificationPost", postNotifications);

		mv.addObject("posts", postid);
		mv.addObject("findcomments", comments);
		mv.setViewName("ViewSelectedPost_friend");
		return mv;

	}
	
	
	@GetMapping(value = "/viewSelectedPost")
	public ModelAndView viewPost(@RequestParam("postid") String postid, HttpServletRequest req) {
		System.out.println("in view selected post");
		ModelAndView mv = new ModelAndView();
		Optional<Post> result = post.findById(Integer.parseInt(postid));
		Post p = result.get();
		
		String ImgSrc = p.getImage();
		System.out.println("post id got is " + postid);

		PostAudio pa = new PostAudio();
		
		Optional<PostAudio> resultaudio = audiorepo.findBypostid(Integer.parseInt(postid));
		
		if (resultaudio.isPresent()) {

			pa = resultaudio.get();
			String AudioSrc = pa.getAudio();
			String text = pa.getText();

			mv.addObject("AudioSrc", AudioSrc);
			if (!(text.equals(null))) {
				mv.addObject("text", text);
			}

		}

		mv.addObject("ImgSrc", ImgSrc);
		HashMap<Integer, String> comments = s.getAllComments(req,postid);
		
		HashMap<String, String> UserPostComments = s.friendCommentOnUserPost(req);
		HashMap<String, String> postNotificationsComments = s.friendCommentOnHisPost(req);
		HashMap<String, String> postNotifications = s.friendPost(req);
		List resultUserPost = s.getUserPost(req);
		mv.addObject("postNotificationsComments", postNotificationsComments);
		mv.addObject("UserPostComments", UserPostComments);
		mv.addObject("notificationPost", postNotifications);
		
		

		mv.addObject("posts", resultUserPost);
		mv.addObject("findcomments", comments);
		mv.setViewName("ViewSelectedPost");
		return mv;

	}

}