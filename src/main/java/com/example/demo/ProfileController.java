package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

//import org.apache.activemq.util.IOExceptionHandler;
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
public class ProfileController {
	
	@Value("#{environment['AWS_ACCESS_KEY_ID']}")
	public String accessKey;
	@Value("#{systemEnvironment['AWS_SECRET_ACCESS_KEY']}")
	String SecretKey;
	
	@GetMapping(value="")
	public ModelAndView renderPage() {
		
		
		ModelAndView IndexPage=new ModelAndView();
		IndexPage.setViewName("index");
		return IndexPage;
	}
	
	@PostMapping(value="/upload")
	public ModelAndView uploadtoS3(@RequestParam("file") MultipartFile image)
	{	ModelAndView ProfilePage=new ModelAndView();
		
	
		
		BasicAWSCredentials cred = new BasicAWSCredentials(accessKey,SecretKey);

		AmazonS3 s3client=AmazonS3ClientBuilder.standard().withCredentials
				(new AWSStaticCredentialsProvider(cred))
				.withRegion(Regions.US_EAST_2) 
				.build();
	try {
		
		PutObjectRequest putReq = new PutObjectRequest("softwareengineeringdemo1",image.getOriginalFilename(),image.getInputStream(),new ObjectMetadata())
		
		.withCannedAcl(CannedAccessControlList.PublicRead);		
		s3client.putObject(putReq);
		
		String ImgSrc="http://"+"softwareengineeringdemo1"+".s3.amazonaws.com/"+image.getOriginalFilename();
		ProfilePage.addObject("ImgSrc",ImgSrc);
		ProfilePage.setViewName("User_Profile");
		return ProfilePage;
	}
	catch(IOException e)
	{
		e.printStackTrace();
		ProfilePage.setViewName("error");
		return ProfilePage;
	}
	
		
	
		
	}
	
	
}
