package com.webflux.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
		System.out.println("Olá, Mundo!");
		String currentDir = System.getProperty("user.dir");
        System.out.println("Diretório raiz do projeto: " +currentDir);
	}

}
