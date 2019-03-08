package com.legosoft.facultades;

import org.axonframework.boot.autoconfig.AMQPAutoConfiguration;
import org.axonframework.boot.autoconfig.AxonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration(exclude={AMQPAutoConfiguration.class})
@SpringBootApplication
public class FacultadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacultadesApplication.class, args);
	}

}
