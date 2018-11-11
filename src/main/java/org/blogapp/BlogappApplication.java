package org.blogapp;

import org.blogapp.Configs.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration
public class BlogappApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BlogappApplication.class, args);
	}
}
