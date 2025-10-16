package com.generation.blogpessoal.controller;

import java.util.List; // Importa a classe List para trabalhar com listas
import java.util.Optional; // Importa a classe Optional para lidar com valores que podem estar ausentes

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

import com.generation.blogpessoal.model.Tema; // Importa a classe Tema da model
import com.generation.blogpessoal.repository.TemaRepository; // Importa a interface TemaRepository da repository
import jakarta.validation.Valid; // Importa a anotação Valid para validação de dados

// Define que essa classe é um controlador REST
@RestController
@RequestMapping("/temas") // Define o endpoint da API
@CrossOrigin(origins = "*", allowedHeaders = "*") // Permite requisições de qualquer origem
public class TemaController { // Define a classe TemaController

	@Autowired // Injeção de dependência do Spring
	private TemaRepository temaRepository; // Instancia o repositório de Tema

	@GetMapping // Mapeia requisições GET para esse método	
	public ResponseEntity<List<Tema>> getAll() { // Define o método getAll que retorna uma lista de Tema dentro de um ResponseEntity
		return ResponseEntity.ok(temaRepository.findAll()); // Retorna a lista de postagens com status 200 (OK)

		// equivalente ao SELECT * FROM db_blogpessoal.tb_temas;
	}

	@GetMapping("/{id}") // Mapeia requisições GET com um ID na URL para esse método
	public ResponseEntity<Tema> getById(@PathVariable Long id) { // Extrai o ID da URL e define o método getById que retorna
		return temaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)) // uma Tema dentro de um ResponseEntity
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna a tema com status 200 (OK) ou 404 (Not Found) se não encontrada
		
		// equivalente ao SELECT * FROM db_blogpessoal.tb_temas WHERE id = ?;	
	}
	
	@GetMapping("/descricao/{descricao}") // Mapeia requisições GET com uma descricao na URL para esse método
	public ResponseEntity<List<Tema>> getByDescricao(@PathVariable String descricao) { // Extrai o título da URL e define o método getByTitulo que retorna uma lista de Tema dentro de um ResponseEntity
		return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao)); // Retorna a lista de postagens que contém o título, ignorando maiúsculas e minúsculas, com status 200 (OK)
		
		// equivalente ao SELECT * FROM db_blogpessoal.tb_temas WHERE descricao LIKE "%descricao%";
	}
	
	@PostMapping // Mapeia requisições POST para esse método
	public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema) { // Define o método post que recebe uma Tema no corpo da requisição e retorna uma Tema dentro de um ResponseEntity
		tema.setId(null); // Garante que o ID seja nulo para criar uma nova tema
		return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema)); // Salva a tema e retorna com status 201 (Created)
		
		// equivalente ao INSERT INTO tb_temas (descricao) VALUES ('Descricao');
	}
	
	@PutMapping // Mapeia requisições PUT para esse método
	public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema) { //	 Define o método put que recebe uma Tema no corpo da requisição e retorna uma Tema dentro de um ResponseEntity
		return temaRepository.findById(tema.getId()) // Verifica se a tema com o ID fornecido existe
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema))) // Se existir, atualiza a tema e retorna com status 200 (OK)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Se não existir, retorna status 404 (Not Found)

		// equivalente ao UPDATE tb_temas SET descricao = 'descricao' WHERE id = ?;
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) // Define o status de resposta como 204 (No Content)
	@DeleteMapping("/{id}") // Mapeia requisições DELETE com um ID na URL para esse método
	public void delete(@PathVariable Long id) { // Extrai o ID da URL e define o método delete que não retorna nada
        Optional<Tema> tema = temaRepository.findById(id); // Ver
        if (tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // Se a tema não existir, lança uma exceção com status 404 (Not Found)
        temaRepository.deleteById(id); // Se existir, deleta o tema pelo ID
        
        // equivalente ao DELETE FROM tb_temas WHERE id = ?;
    }	
}
