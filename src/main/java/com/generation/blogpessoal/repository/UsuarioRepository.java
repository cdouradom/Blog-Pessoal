package com.generation.blogpessoal.repository; // Define o pacote onde a interface está localizada

import java.util.Optional; // Importa a classe Optional para representar valores que podem estar presentes ou ausentes 

import org.springframework.data.jpa.repository.JpaRepository; // Importa a interface JpaRepository do Spring Data JPA

import com.generation.blogpessoal.model.Usuario; // Importa a classe Usuario do pacote model

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { // Interface que estende JpaRepository para fornecer operações CRUD para a entidade Usuario

    Optional<Usuario> findByUsuario(String usuario); // Método para encontrar um usuário pelo seu nome de usuário

}