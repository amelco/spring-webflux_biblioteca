package com.webflux.biblioteca.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {
	
	private String id;
	
	private String titulo;
	
	private String autor;
	
}
