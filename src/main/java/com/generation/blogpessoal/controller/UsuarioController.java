package com.generation.blogpessoal.controller; // Define o pacote onde a classe está localizada

import java.util.List; // Importa a interface List para trabalhar com listas
import java.util.Optional; // Importa a classe Optional para representar valores que podem estar presentes ou ausentes
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para representar códigos de status HTTP
import org.springframework.http.ResponseEntity; // Importa a classe ResponseEntity para representar respostas HTTP
import org.springframework.web.bind.annotation.CrossOrigin; // Importa a anotação CrossOrigin para configurar o compartilhamento de recursos entre origens
import org.springframework.web.bind.annotation.RequestMapping; // Importa a anotação RequestMapping para mapear URLs para controladores
import org.springframework.web.bind.annotation.RestController; // Importa a anotação RestController para marcar a classe como um controlador REST
import com.generation.blogpessoal.model.Usuario; // Importa a classe Usuario do pacote model
import com.generation.blogpessoal.model.UsuarioLogin; // Importa a classe UsuarioLogin do pacote model
import com.generation.blogpessoal.service.UsuarioService; // Importa a classe UsuarioService do pacote service
import jakarta.validation.Valid; // Importa a anotação Valid para validação de dados de entrada
import org.springframework.web.bind.annotation.GetMapping; // Importa a anotação GetMapping para mapear requisições HTTP GET
import org.springframework.web.bind.annotation.PathVariable; // Importa a anotação PathVariable para extrair variáveis de caminho das URLs
import org.springframework.web.bind.annotation.PostMapping; // Importa a anotação PostMapping para mapear requisições HTTP POST
import org.springframework.web.bind.annotation.PutMapping; // Importa a anotação PutMapping para mapear requisições HTTP PUT
import org.springframework.web.bind.annotation.RequestBody; // Importa a anotação RequestBody para vincular o corpo da requisição a um objeto

// Marca a classe como um controlador REST para gerenciar requisições relacionadas a usuários
@RestController // Define a classe UsuarioController como um controlador REST
@RequestMapping("/usuarios") // Mapeia as requisições para o caminho "/usuarios"
@CrossOrigin(origins = "*", allowedHeaders = "*") // Permite requisições de qualquer origem e com quaisquer cabeçalhos
public class UsuarioController { // Início da classe UsuarioController 

    // Injeção de dependência do serviço de usuários para manipulação dos dados
    @Autowired // Injeta automaticamente a dependência do UsuarioService 
    private UsuarioService usuarioService; // Declara o serviço de usuários

    // Métodos do controlador para gerenciar requisições HTTP relacionadas a usuários
    @GetMapping("all") // Mapeia requisições HTTP GET para o caminho "/all" 
    public ResponseEntity<List<Usuario>> getAll() { // Método para obter todos os usuários
        return ResponseEntity.ok(usuarioService.getAll()); // Retorna a lista de todos os usuários com status HTTP 200 (OK)
    }

    @GetMapping("id") // Mapeia requisições HTTP GET para o caminho "/id"
    public ResponseEntity<Usuario> getById(@PathVariable Long id) { // Método para obter um usuário pelo seu ID
        return usuarioService.getById(id) // Chama o serviço para obter o usuário pelo ID
                .map(resposta -> ResponseEntity.ok(resposta)) // Se o usuário for encontrado, retorna com status HTTP 200 (OK)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Se o usuário não for encontrado, retorna status HTTP 404 (Not Found)
    }

    @PostMapping("/cadastrar") // Mapeia requisições HTTP POST para o caminho "/cadastrar"
    public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {  // Método para cadastrar um novo usuário
        return usuarioService.cadastrarUsuario(usuario) // Chama o serviço para cadastrar o novo usuário
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta)) // Se o cadastro for bem-sucedido, retorna o usuário com status HTTP 201 (Created)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()); // Se o cadastro falhar (por exemplo, usuário já existe), retorna status HTTP 400 (Bad Request)
    }

    @PutMapping("/atualizar") // Mapeia requisições HTTP PUT para o caminho "/atualizar"
    public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) { // Método para atualizar um usuário existente
        return usuarioService.atualizarUsuario(usuario) // Chama o serviço para atualizar o usuário
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta)) // Se a atualização for bem-sucedida, retorna o usuário atualizado com status HTTP 200 (OK)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Se o usuário não for encontrado para atualização, retorna status HTTP 404 (Not Found)
    }

    @PostMapping("/logar") // Mapeia requisições HTTP POST para o caminho "/logar"
    public ResponseEntity<UsuarioLogin> autenticar(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin) { // Método para autenticar um usuário
        return usuarioService.autenticarUsuario(usuarioLogin) // Chama o serviço para autenticar o usuário
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta)) // Se a autenticação for bem-sucedida, retorna os dados de login com status HTTP 200 (OK)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // Se a autenticação falhar, retorna status HTTP 401 (Unauthorized)
    }
}
