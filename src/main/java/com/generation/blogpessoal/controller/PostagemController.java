package com.generation.blogpessoal.controller; // Define o pacote onde a classe está localizada

import java.util.List; // Importa a classe List para trabalhar com listas
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para manipular respostas HTTP
import org.springframework.web.bind.annotation.CrossOrigin; // Importa a anotação CrossOrigin para configurar CORS
import org.springframework.web.bind.annotation.GetMapping; // Importa a anotação GetMapping para mapear requisições GET
import org.springframework.web.bind.annotation.PathVariable; //	 Importa a anotação PathVariable para extrair variáveis da URL
import org.springframework.web.bind.annotation.RequestMapping; // Importa a anotação RequestMapping para mapear URLs
import org.springframework.web.bind.annotation.RestController; // Importa a anotação RestController para definir um controlador REST
import com.generation.blogpessoal.model.Postagem; // Importa a classe Postagem da model
import com.generation.blogpessoal.repository.PostagemRepository; // Importa a interface PostagemRepository da repository

// Define que essa classe é um controlador REST
@RestController
@RequestMapping("/postagens") // Define o endpoint da API
@CrossOrigin(origins = "*", allowedHeaders = "*") //
public class PostagemController { // Define a classe PostagemController

	@Autowired // Injeção de dependência do Spring
	private PostagemRepository postagemRepository; // Instancia o repositório de Postagem

	@GetMapping // Mapeia requisições GET para esse método	
	public ResponseEntity<List<Postagem>> getAll() { // Define o método getAll que retorna uma lista de Postagem dentro de um ResponseEntity
		return ResponseEntity.ok(postagemRepository.findAll()); // Retorna a lista de postagens com status 200 (OK)

		// SELECT * FROM db_blogpessoal.tb_postagens;
	}

	@GetMapping("/{id}") // Mapeia requisições GET com um ID na URL para esse método
	public ResponseEntity<Postagem> getById(@PathVariable Long id) { // Extrai o ID da URL e define o método getById que retorna
		return postagemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta)) // uma Postagem dentro de um ResponseEntity
				.orElse(ResponseEntity.notFound().build()); // Retorna a postagem com status 200 (OK) ou 404 (Not Found) se não encontrada
	}
	// SELECT * FROM db_blogpessoal.tb_postagens WHERE id = id;
}
