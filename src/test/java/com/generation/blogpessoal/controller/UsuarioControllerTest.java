package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    private static final String BASE_URL = "/usuarios";
    private static final String ADMIN = "root@root.com";
    private static final String SENHA = "rootroot";

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();
        usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", ADMIN, SENHA));
    }

    @Test
    @DisplayName(" 01 - Deve cadastrar um novo usuario com sucesso")
    void deveCadastrarUsuario() {

        // Given
        Usuario usuario = TestBuilder.criarUsuario(null, "Maria Joana", "maria.joana@email.com", "12345678");

        //When
        HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
        ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
            BASE_URL + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        //Then
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
    }
         
    @Test
    @DisplayName("02 - Nao deve permitir a duplicidade do usuario")
    void naoDeveDuplicarUsuario(){

        //Given
        Usuario usuario = TestBuilder.criarUsuario(null, "Julia Alves", "julia.alves@email.com", "12345678");
        usuarioService.cadastrarUsuario(usuario);

        //When
        HttpEntity<Usuario> requisicao = new HttpEntity<>(usuario);
        ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
            BASE_URL + "/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    @DisplayName("03 - Deve atualizar os dados de um usuario com sucesso")
    void naoDeveAtualizarUmUsuario(){

        //Given
        Usuario usuario = TestBuilder.criarUsuario(null, "Juliana Morada", "juliana.morada@email.com", "12345678");
        Optional<Usuario> cadastrado = usuarioService.cadastrarUsuario(usuario);

        Usuario usuarioUpdate = TestBuilder.criarUsuario(cadastrado.get().getId(), "Juliana Ramos",
             "ju.ramos@email.com", "12345678");

        //When
        String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
        HttpEntity<Usuario> requisicao = JwtHelper.criarRequisicaoComToken(usuarioUpdate, token);
        ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
            BASE_URL + "/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

        //Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
    }

    @Test
    @DisplayName("04 - Deve listar todos os usuarios com sucesso")
    void deveListarTodosUsuarios(){

        //Given
        usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Ana Lima", "ana.lima@email.com", "12345678"));
        usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Bruna Lopes", "bruna.lopes@email.com", "12345678"));

        //When
        String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
        HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
        ResponseEntity<Usuario[]> resposta = testRestTemplate.exchange(
            BASE_URL + "/all", HttpMethod.GET, requisicao,Usuario[].class);

        //Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
    }

    @Test
    @DisplayName("05 - Deve listar um usuário específico pelo id")
    void deveListarUsuarioPorId() {

        // Given
        Usuario usuario = TestBuilder.criarUsuario(null, "Carlos Souza", "carlos.souza@email.com", "12345678");
        Optional<Usuario> usuarioCriado = usuarioService.cadastrarUsuario(usuario);

        // When
        String token = JwtHelper.obterToken(testRestTemplate, ADMIN, SENHA);
        HttpEntity<Void> requisicao = JwtHelper.criarRequisicaoComToken(token);
        ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
            BASE_URL + "/id/" + usuarioCriado.get().getId(), HttpMethod.GET, requisicao, Usuario.class);

        // Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(usuarioCriado.get().getId(), resposta.getBody().getId());
        assertEquals("Carlos Souza", resposta.getBody().getNome());
}
    @Test
    @DisplayName("06 - Deve autenticar um usuário com sucesso")
    void deveAutenticarUsuario() {

        // Given
        Usuario usuario = TestBuilder.criarUsuario(null, "Mariana Teixeira", "mariana.teixeira@email.com", "12345678");
        usuarioService.cadastrarUsuario(usuario);

        Usuario usuarioLogin = TestBuilder.criarUsuario(null, null, "mariana.teixeira@email.com", "12345678");

        // When
        HttpEntity<Usuario> requisicao = new HttpEntity<>(usuarioLogin);
        ResponseEntity<String> resposta = testRestTemplate.exchange(
            BASE_URL + "/logar", HttpMethod.POST, requisicao, String.class);

        // Then
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(true, resposta.getBody().contains("token")); // opcional: verificar se há um token no corpo da resposta
    }
    
}