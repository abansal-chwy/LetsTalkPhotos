package com.example.demo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.UserProfile;

@Controller
public class UserProfileController {

	@Autowired
	private UserProfileRepository userprofile;
	
	@Autowired
	private FriendsRepository friendRepo;
	
	@Value("#{environment['AWS_ACCESS_KEY_ID']}")
	public String accessKey;
	@Value("#{systemEnvironment['AWS_SECRET_ACCESS_KEY']}")
	String SecretKey;
	
	
	@GetMapping(value = "/VerifyProfile")
	public ModelAndView VerifyProfile(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();

		String UserID = (String) req.getSession().getAttribute("myId");
		Optional<UserProfile> result = userprofile.findByUserID(UserID.toString());// optional-since may not be present
		if (result.isPresent()) {
			return new ModelAndView("redirect:/RetreiveProfile");
		} else {
			
			return new ModelAndView("redirect:/UserProfile");

		}

	}

	
	
	@GetMapping(value = "/UserProfile")
	public ModelAndView ProfilePage() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("Create_Profile");
		return mv;
	}

	
	@PostMapping(value = "/SubmitProfile")
	public ModelAndView UserProfile(@RequestParam(name = "bio", required = true) String bio,
			@RequestParam("file") MultipartFile image,HttpServletRequest req) {
		ModelAndView mv= new ModelAndView();
		BasicAWSCredentials cred = new BasicAWSCredentials(accessKey,SecretKey);

		AmazonS3 s3client=AmazonS3ClientBuilder.standard().withCredentials
				(new AWSStaticCredentialsProvider(cred))
				.withRegion(Regions.US_EAST_2) 
				.build();
		try {
			
			PutObjectRequest putReq = new PutObjectRequest("softwareengineeringdemo1",image.getOriginalFilename(),image.getInputStream(),new ObjectMetadata())
			
			.withCannedAcl(CannedAccessControlList.PublicRead);		
			s3client.putObject(putReq);
		
		UserProfile up = new UserProfile();
		String UserID = (String) req.getSession().getAttribute("myId");
		String name = (String) req.getSession().getAttribute("name");
		up.setUserID(UserID);
		up.setName(name);
		up.setBiography(bio);
		up.setImage(image.getOriginalFilename());
		userprofile.save(up);
		
		}
		catch(IOException e)
		{
			e.printStackTrace();
			mv.setViewName("error");
			return mv;
		}

		return new ModelAndView("redirect:/RetreiveProfile");

	}

	@GetMapping(value = "/RetreiveProfile")
	public ModelAndView getUserProfile(HttpServletRequest req) {
		
		ModelAndView retrieve = new ModelAndView();
		
		
		String UserID = (String) req.getSession().getAttribute("myId");
		Optional<UserProfile> result = userprofile.findByUserID(UserID.toString());// optional-since may not be present
		UserProfile u = result.get();
		//System.out.println(u.getImage());
		String ImgSrc="http://"+"softwareengineeringdemo1"+".s3.amazonaws.com/"+u.getImage();
		retrieve.addObject("ImgSrc",ImgSrc);		
		retrieve.addObject("user", u);
		retrieve.setViewName("User_Profile");
		return retrieve;

	}
	@GetMapping(value = "/Friends")
	public ModelAndView getFriends(HttpServletRequest req) {
	
	ModelAndView mv = new ModelAndView();
	String UserID = (String) req.getSession().getAttribute("myId");
	List<Friends> friendsresult=friendRepo.findAllByUser(UserID.toString());

	if (!(friendsresult.size()==0)) {
		mv.addObject("friends",friendsresult);
		mv.setViewName("Friends");
		return mv;
	}	
	else {
		return new ModelAndView("ViewFriendsError");
	}
	}
}