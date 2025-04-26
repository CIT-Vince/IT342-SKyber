package edu.cit.SKyber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import edu.cit.SKyber.Config.FirebaseConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(FirebaseConfigProperties.class)
public class SKyberApplication {

	public static void main(String[] args) {
		SpringApplication.run(SKyberApplication.class, args);
	}

}
