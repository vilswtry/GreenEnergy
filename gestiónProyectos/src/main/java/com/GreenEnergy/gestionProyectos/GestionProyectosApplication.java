package com.GreenEnergy.gestionProyectos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class GestionProyectosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionProyectosApplication.class, args);
	}

}
