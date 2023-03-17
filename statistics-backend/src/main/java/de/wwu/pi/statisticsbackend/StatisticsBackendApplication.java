package de.wwu.pi.statisticsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class StatisticsBackendApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(StatisticsBackendApplication.class, args);
	}

}
