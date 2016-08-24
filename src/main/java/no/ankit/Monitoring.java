package no.ankit;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Monitoring {

	public static void main(String[] args) {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		System.setProperty("spring.profiles.active","local");
		run(Monitoring.class, args);
	}

}
