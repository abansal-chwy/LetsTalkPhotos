package com.example.demo;


import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class uploadToS3 {
	
	@Value("#{environment['AWS_ACCESS_KEY_ID']}")
	public String accessKey;
	@Value("#{systemEnvironment['AWS_SECRET_ACCESS_KEY']}")
	String SecretKey;
	
	public String uploadAudio(String Filename, InputStream inputStream) {
			
		String fileURI;
		BasicAWSCredentials cred = new BasicAWSCredentials(accessKey,SecretKey);

		AmazonS3 s3client=AmazonS3ClientBuilder.standard().withCredentials
				(new AWSStaticCredentialsProvider(cred))
				.withRegion(Regions.US_EAST_2) 
				.build();
	PutObjectRequest putReq = new PutObjectRequest("softwareengineeringdemo1",Filename,inputStream,new ObjectMetadata())
	
	.withCannedAcl(CannedAccessControlList.PublicRead);		
	s3client.putObject(putReq);
	
	 fileURI="http://"+"softwareengineeringdemo1"+".s3.amazonaws.com/"+Filename;
	return fileURI;
	
		
	
		
	
	}
}
