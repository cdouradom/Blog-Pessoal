package com.generation.blogpessoal.service; // Define o pacote onde a classe está localizada

import java.util.List; // Importa a classe List para trabalhar com listas
import java.util.Optional; // Importa a classe Optional para representar valores que podem estar presentes ou ausentes
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para representar códigos de status HTTP
import org.springframework.security.authentication.AuthenticationManager; // Importa a classe AuthenticationManager para gerenciar autenticações
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa a classe UsernamePasswordAuthenticationToken para representar tokens de autenticação
import org.springframework.security.crypto.password.PasswordEncoder; // Importa a interface PasswordEncoder para codificação de senhas
import org.springframework.stereotype.Service; // Importa a anotação Service para marcar a classe como um serviço
import org.springframework.web.server.ResponseStatusException; // Importa a classe ResponseStatusException para lançar exceções com status HTTP
import com.generation.blogpessoal.model.Usuario; // Importa a classe Usuario do pacote model
import com.generation.blogpessoal.model.UsuarioLogin; // Importa a classe UsuarioLogin do pacote model
import com.generation.blogpessoal.repository.UsuarioRepository; // Importa a interface UsuarioRepository do pacote repository
import com.generation.blogpessoal.security.JwtService; // Importa a classe JwtService do pacote security

// Marca a classe como um serviço do Spring
@Service // Define a classe UsuarioService como um serviço do Spring
public class UsuarioService { // Início da classe UsuarioService

    // Injeção de dependência do repositório de usuários e outros serviços
    // necessários
    @Autowired // Injeta automaticamente a dependência do UsuarioRepository
    private UsuarioRepository usuarioRepository; // Declara o repositório de usuários

    @Autowired // Injeta automaticamente a dependência do JwtService
    private JwtService jwtService; // Declara o serviço JWT

    @Autowired // Injeta automaticamente a dependência do AuthenticationManager
    private AuthenticationManager authenticationManager; // Declara o gerenciador de autenticação

    @Autowired // Injeta automaticamente a dependência do PasswordEncoder
    private PasswordEncoder passwordEncoder; // Declara o codificador de senhas

    // Métodos do serviço para operações relacionadas a usuários

    public List<Usuario> getAll() { // Método para obter todos os usuários
        return usuarioRepository.findAll(); // Retorna a lista de todos os usuários do repositório
    }

    public Optional<Usuario> getById(Long id) { // Método para obter um usuário pelo seu ID
        return usuarioRepository.findById(id); // Retorna um Optional contendo o usuário encontrado ou vazio se não encontrado
    }

    // Método para cadastrar um novo usuário
    public Optional<Usuario> cadastrarUsuario(Usuario usuario) { // Método para cadastrar um novo usuário

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) { // Verifica se o usuário já existe pelo nome de usuário
            return Optional.empty(); // Retorna um Optional vazio se o usuário já existir
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Codifica a senha do usuário antes de salvar
        usuario.setId(null); // Garante que o ID seja nulo para criação de um novo usuário

        return Optional.of(usuarioRepository.save(usuario)); // Salva o novo usuário no repositório e retorna um Optional contendo o usuário salvo
    }

    // Método para atualizar um usuário existente
    public Optional<Usuario> atualizarUsuario(Usuario usuario) { // Método para atualizar um usuário existente

        if (!usuarioRepository.findById(usuario.getId()).isPresent()) { // Verifica se o usuário existe pelo ID
            return Optional.empty(); // Retorna um Optional vazio se o usuário não existir
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsuario(usuario.getUsuario()); // Verifica se o nome de usuário já está em uso por outro usuário

        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) { // Se o nome de usuário estiver em uso por outro usuário
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já existe para outro usuário!", null); // Lança ma exceção indicando que o usuário já existe
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Codifica a senha do usuário antes de salvar
        return Optional.of(usuarioRepository.save(usuario)); // Salva as alterações do usuário no repositório e retorna um Optional contendo o usuário atualizado
    }

    // Método para autenticar um usuário e gerar um token JWT
    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) { // Método para autenticar um usuário

        if (!usuarioLogin.isPresent()) { // Verifica se o objeto UsuarioLogin está presente
            return Optional.empty(); // Retorna um Optional vazio se o objeto não estiver presente
        }

        UsuarioLogin login = usuarioLogin.get(); // Obtém o objeto UsuarioLogin do Optional

        try { // Tenta autenticar o usuário com as credenciais fornecidas

            authenticationManager.authenticate( // Realiza a autenticação do usuário
                    new UsernamePasswordAuthenticationToken(login.getUsuario(), login.getSenha())); // Cria um token de autenticação com o nome de usuário e senha fornecidos

            return usuarioRepository.findByUsuario(login.getUsuario()) // Busca o usuário no repositório pelo nome de usuário
                    .map(usuario -> construirRespostaLogin(login, usuario)); // Constrói a resposta de login com os dados do usuário autenticado

        } catch (Exception e) { // Captura qualquer exceção que ocorra durante a autenticação

            return Optional.empty(); // Retorna um Optional vazio se a autenticação falhar
        }
    }

    // Métodos auxiliares para construção de respostas e geração de tokens
    private UsuarioLogin construirRespostaLogin(UsuarioLogin usuarioLogin, Usuario usuario) { // Método auxiliar para construir a resposta de login

        usuarioLogin.setId(usuario.getId()); // Define o ID do usuário no objeto UsuarioLogin
        usuarioLogin.setNome(usuario.getNome()); // Define o nome do usuário no objeto UsuarioLogin
        usuarioLogin.setFoto(usuario.getFoto()); // Define a foto do usuário no objeto UsuarioLogin
        usuarioLogin.setSenha(""); // Limpa a senha no objeto UsuarioLogin por segurança
        usuarioLogin.setToken(gerarToken(usuario.getUsuario())); // Gera e define o token JWT no objeto UsuarioLogin
        return usuarioLogin; // Retorna o objeto UsuarioLogin preenchido

    }

    // Método auxiliar para gerar um token JWT para o usuário autenticado
    private String gerarToken(String usuario) { // Método auxiliar para gerar um token JWT para o usuário autenticado
        return "Bearer " + jwtService.generateToken(usuario); // Gera o token JWT e o prefixa com "Bearer "
    }
}