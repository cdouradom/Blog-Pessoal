package com.generation.blogpessoal.repository; // Define o pacote onde a classe está localizada

import java.util.List; // Importa a classe List para trabalhar com listas

import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository do Spring Data JPA
import org.springframework.data.repository.query.Param; // Importa a anotação Param para definir parâmetros em consultas

import com.generation.blogpessoal.model.Tema;// Importa a classe Tema da model


//Com qual model (tabela) essa Interface irá trabalhar, Long como a chave primária, no nosso caso é o id
// JpaRepository já possui vários métodos prontos para serem usados (CRUD)
public interface TemaRepository extends JpaRepository<Tema, Long>{ // Define a interface PostagemRepository que estende JpaRepository 
	
	// Query Methods
	public List<Tema> findAllByDescricaoContainingIgnoreCase(@Param("descricao")String descricao); // Método para buscar postagens por descricao, ignorando maiúsculas e minúsculas

}