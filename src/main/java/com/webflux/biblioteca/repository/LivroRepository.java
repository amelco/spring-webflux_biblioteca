package com.webflux.biblioteca.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.webflux.biblioteca.model.Livro;

public interface LivroRepository extends ReactiveMongoRepository<Livro, String>{

}
