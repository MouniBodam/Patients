package com.hospital_management;

//import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalManagementApplication {

	public static void main(String[] args) {
		//SpringApplication app = new SpringApplication(HospitalManagementApplication.class);
       // app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        // app.run(args);
		SpringApplication.run(HospitalManagementApplication.class, args);
	}

}
