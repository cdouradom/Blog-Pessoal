package com.generation.blogpessoal.controller; // Define o pacote onde a classe está localizada

import java.util.List; // Importa a classe List para trabalhar com listas
import java.util.Optional; // Importa a classe Optional para trabalhar com valores que podem estar ausentes

import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.http.HttpStatus; // Importa a enumeração HttpStatus para definir códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para manipular respostas HTTP
import org.springframework.web.bind.annotation.CrossOrigin; // Importa a anotação CrossOrigin para configurar CORS
import org.springframework.web.bind.annotation.DeleteMapping; // Importa a anotação DeleteMapping para mapear requisições DELETE
import org.springframework.web.bind.annotation.GetMapping; // Importa a anotação GetMapping para mapear requisições GET
import org.springframework.web.bind.annotation.PathVariable; //	 Importa a anotação PathVariable para extrair variáveis da URL
import org.springframework.web.bind.annotation.PostMapping; // Importa a anotação PostMapping para mapear requisições POST
import org.springframework.web.bind.annotation.PutMapping; // Importa a anotação PutMapping para mapear requisições PUT
import org.springframework.web.bind.annotation.RequestBody; // Importa a anotação RequestBody para extrair o corpo da requisição
import org.springframework.web.bind.annotation.RequestMapping; // Importa a anotação RequestMapping para mapear URLs
import org.springframework.web.bind.annotation.ResponseStatus; // Importa a anotação ResponseStatus para definir o status de resposta padrão
import org.springframework.web.bind.annotation.RestController; // Importa a anotação RestController para definir um controlador REST
import org.springframework.web.server.ResponseStatusException; // Importa a classe ResponseStatusException para lançar exceções com status HTTP

import com.generation.blogpessoal.model.Postagem; // Importa a classe Postagem da model
import com.generation.blogpessoal.repository.PostagemRepository; // Importa a interface PostagemRepository da repository
import com.generation.blogpessoal.repository.TemaRepository; // Importa a interface TemaRepository da repository

import jakarta.validation.Valid; // Importa a anotação Valid para validação de dados

// Define que essa classe é um controlador REST
@RestController
@RequestMapping("/postagens") // Define o endpoint da API
@CrossOrigin(origins = "*", allowedHeaders = "*") //
public class PostagemController { // Define a classe PostagemController

	@Autowired // Injeção de dependência do Spring
	private PostagemRepository postagemRepository; // Instancia o repositório de Postagem

	@Autowired // Injeção de dependência do Spring
	private TemaRepository temaRepository; // Instancia o repositório de Tema

	@GetMapping // Mapeia requisições GET para esse método
	public ResponseEntity<List<Postagem>> getAll() { // Define o método getAll que retorna uma lista de Postagem dentro de um ResponseEntity
		return ResponseEntity.ok(postagemRepository.findAll()); // Retorna a lista de postagens com status 200 (OK)
		// equivalente ao SELECT * FROM db_blogpessoal.tb_postagens;
	}

	@GetMapping("/{id}") // Mapeia requisições GET com um ID na URL para esse método
	public ResponseEntity<Postagem> getById(@PathVariable Long id) { // Extrai o ID da URL e define o método getById que retorna
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)) // uma Postagem dentro de um ResponseEntity
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna a postagem com status 200 (OK) ou 404 (Not Found) se não encontrada
		// equivalente ao SELECT * FROM db_blogpessoal.tb_postagens WHERE id = ?;
	}

	@GetMapping("/titulo/{titulo}") // Mapeia requisições GET com um título na URL para esse método
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) { // Extrai o título da URL e define o método getByTitulo que retorna uma lista de Postagem dentro de um ResponseEntity
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); // Retorna a lista de postagens que contém o título, ignorando maiúsculas e minúsculas, com status 200 (OK)
		// equivalente ao SELECT * FROM db_blogpessoal.tb_postagens WHERE titulo LIKE "%titulo%";
	}

	@PostMapping // Mapeia requisições POST para esse método
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) { // Define o método post que recebe uma Postagem no corpo da requisição e retorna uma Postagem dentro de um ResponseEntity
		if (temaRepository.existsById(postagem.getTema().getId())) { // Verifica se o tema associado à postagem existe
			postagem.setId(null); // Garante que o ID seja nulo para criar uma nova postagem
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)); // Salva a postagem e retorna com status 201 (Created)
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema inexistente", null); // Se o tema não existir, lança uma exceção com status 400 (Bad Request)
		// equivalente ao INSERT INTO tb_postagens (data, texto, titulo, tema) VALUES (current_timestamp(), 'Texto', 'Titulo', 'Tema');
	}

	@PutMapping // Mapeia requisições PUT para esse método
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) { // Define o método put que recebe uma Postagem no corpo da requisição e retorna uma Postagem dentro de um ResponseEntity
		if (postagemRepository.existsById(postagem.getId())) { // Verifica se a postagem com o ID fornecido existe
			if (temaRepository.existsById(postagem.getTema().getId())) { // Verifica se o tema associado à postagem existe
				return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)); // Se ambos existirem, atualiza a postagem e retorna com status 200 (OK)
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema inexistente", null);
		}
		return ResponseEntity.notFound().build(); // Se a postagem não existir, retorna status 404 (Not Found)	
		// equivalente ao UPDATE tb_postagens SET data = current_timestamp(), texto = 'Texto', titulo = 'Titulo', tema = 'Tema' WHERE id = ?;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT) // Define o status de resposta como 204 (No Content)
	@DeleteMapping("/{id}") // Mapeia requisições DELETE com um ID na URL para esse método
	public void delete(@PathVariable Long id) { // Extrai o ID da URL e define o método delete que não retorna nada
		Optional<Postagem> postagem = postagemRepository.findById(id); // Busca a postagem pelo ID
		if (postagem.isEmpty()) // Verifica se a postagem existe
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); // Se a postagem não existir, lança uma exceção com status 404 (Not Found)
		postagemRepository.deleteById(id); // Se existir, deleta a postagem pelo ID
		// equivalente ao DELETE FROM tb_postagens WHERE id = ?;
	}
}
