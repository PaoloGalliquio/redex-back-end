package com.redexbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.redexbackend.RedexBackEndApplication;
import com.amazonaws.auth.BasicSessionCredentials;

@Configuration
public class StorageConfig {
  // @Value("${cloud.aws.credentials.access-key}")
  // private String accessKey;

  // @Value("${cloud.aws.credentials.secret-key}")
  // private String accessSecret;

  // @Value("${cloud.aws.credentials.token}")
  // private String token;

  // @Value("${cloud.aws.region.static}")
  // private String region;

  // @Bean
  // public AmazonS3 s3Client() {
  //   //AWSCredentials credentials = new BasicSessionCredentials(RedexBackEndApplication.aws_access_key,RedexBackEndApplication.aws_secret_key, RedexBackEndApplication.aws_session_token);
  //   // AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,
  //   // this.accessSecret);
  //   //return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
  //   return null;
  // }
}
