package me.TAJLN.Vremenko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoConfiguration
@SpringBootApplication
public class VremenkoAPI {

	public static void main(String[] args) {
		SpringApplication.run(VremenkoAPI.class, args);
	}

}
