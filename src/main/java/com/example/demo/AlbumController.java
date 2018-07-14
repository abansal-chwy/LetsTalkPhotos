package com.example.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Base64.Decoder;

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

@Controller
public class AlbumController {
	
	@Autowired 
	ServiceClass s;
	@Autowired
	private AlbumRepostiory albumrepo;

	@Value("#{environment['AWS_ACCESS_KEY_ID']}")
	public String accessKey;
	@Value("#{systemEnvironment['AWS_SECRET_ACCESS_KEY']}")
	String SecretKey;
	
	
	@PostMapping(value="/CreateAlbum")
	public ModelAndView createAlbum() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Album");
		return mv;
		
	}


		
		@PostMapping(value="/uploadToAlbum")
		public ModelAndView uploadAlbum(@RequestParam("recording") String recording,
				@RequestParam("file") MultipartFile image,HttpServletRequest req) throws Exception {
			ModelAndView mv= new ModelAndView();
			
			if (recording.isEmpty())
				throw new Exception("recoding data is empty");
			Decoder decoder = Base64.getDecoder();
			System.out.println(recording);
			byte[] decodeByte = decoder.decode(recording.split(",")[1]);
			BasicAWSCredentials cred = new BasicAWSCredentials(accessKey,SecretKey);

			AmazonS3 s3client=AmazonS3ClientBuilder.standard().withCredentials
					(new AWSStaticCredentialsProvider(cred))
					.withRegion(Regions.US_EAST_2) 
					.build();
			
			
try {
				
				PutObjectRequest putReq = new PutObjectRequest("softwareengineeringdemo1",image.getOriginalFilename(),image.getInputStream(),new ObjectMetadata())
				
				.withCannedAcl(CannedAccessControlList.PublicRead);		
				s3client.putObject(putReq);
			
			
	
				FileOutputStream fos;
				fos = new FileOutputStream("MyAudio.webm");
				fos.write(decodeByte);
				fos.close();

				Random rand = new Random();
				int value = rand.nextInt(100000);

				PutObjectRequest putReq2 = new PutObjectRequest("softwareengineeringdemo1", image.getOriginalFilename()+".webm",
						new FileInputStream("MyAudio.webm"), new ObjectMetadata())

								.withCannedAcl(CannedAccessControlList.PublicRead);
				s3client.putObject(putReq2);
			

				Album A = new Album();
				String UserID = (String) req.getSession().getAttribute("myId");
				
				A.setAudio("http://softwareengineeringdemo1.s3.amazonaws.com/"+image.getOriginalFilename()+".webm");
				A.setUserid(UserID);
				A.setImage("http://softwareengineeringdemo1.s3.amazonaws.com/"+image.getOriginalFilename());
				albumrepo.save(A);
			}
				catch(IOException e)
				{
					e.printStackTrace();
					mv.setViewName("error");
					
				}
			return new ModelAndView("redirect:/getAlbum");

			
		}
	
	@GetMapping(value="/getAlbum")
	public ModelAndView getAlbum(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("Album");
		return mv;
	}
	
	@PostMapping(value="/veiwAlbumCreated")
	public ModelAndView viewAlbum(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		String UserID = (String) req.getSession().getAttribute("myId");
		List<Album> results=albumrepo.findAllByUserid(UserID);
		System.out.println("result size isssss"+results.size());
		if(results.size()!=0)
		{
		mv.addObject("album",results);
		mv.setViewName("ViewAlbum");
		}
		
		else {
			mv.setViewName("NoAlbum");
		}
		
		return mv;
	}
	@GetMapping(value = "/viewSelectedPostAlbum")
	public ModelAndView viewPost(@RequestParam("postid") String postid, HttpServletRequest req) {
		System.out.println("in view selected post");
		ModelAndView mv = new ModelAndView();
		Optional<Album> post = albumrepo.findById(Integer.parseInt(postid));
		Album p = post.get();
		
		String ImgSrc = p.getImage();
		

		
		
		
		

			
			String AudioSrc = p.getAudio();
			
			mv.addObject("AudioSrc", AudioSrc);
			

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
		mv.setViewName("ViewSelectedPostAlbum");
		return mv;

	}

}
