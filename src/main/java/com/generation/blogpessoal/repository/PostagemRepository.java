package com.generation.blogpessoal.repository; // Define o pacote onde a classe está localizada

import java.util.List; // Importa a classe List para trabalhar com listas

import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository do Spring Data JPA
import org.springframework.data.repository.query.Param; // Importa a anotação Param para definir parâmetros em consultas

import com.generation.blogpessoal.model.Postagem; // Importa a classe Postagem da model

//Com qual model (tabela) essa Interface irá trabalhar, Long como a chave primária, no nosso caso é o id
// JpaRepository já possui vários métodos prontos para serem usados (CRUD)
public interface PostagemRepository extends JpaRepository<Postagem, Long>{ // Define a interface PostagemRepository que estende JpaRepository 
	
	// Query Methods
	public List<Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo")String titulo); // Método para buscar postagens por título, ignorando maiúsculas e minúsculas
	
}