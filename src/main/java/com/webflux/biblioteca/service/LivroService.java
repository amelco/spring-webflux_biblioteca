package com.webflux.biblioteca.service;

import com.webflux.biblioteca.model.Livro;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LivroService {
	
	Flux<Livro> findAll();
	
	Mono<Livro> findById(String id);
	
	Mono<Livro> save(Livro livro);
	
	Mono<Void> deleteById(String id);

}
