package org.blogapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAutoConfiguration
public class BlogappApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(BlogappApplication.class, args);
	}
}
