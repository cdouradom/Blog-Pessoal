package com.generation.blogpessoal.security; // Define o pacote onde a classe está localizada

import java.util.Optional; // Importa a classe Optional para representar valores que podem estar presentes ou ausentes
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.security.core.userdetails.UserDetails; // Importa a interface UserDetails para representar os detalhes do usuário
import org.springframework.security.core.userdetails.UserDetailsService; // Importa a interface UserDetailsService para carregar os detalhes do usuário
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa a exceção UsernameNotFoundException para indicar que o usuário não foi encontrado
import org.springframework.stereotype.Service; // Importa a anotação Service para marcar a classe como um serviço
import com.generation.blogpessoal.model.Usuario; // Importa a classe Usuario do pacote model
import com.generation.blogpessoal.repository.UsuarioRepository; // Importa a interface UsuarioRepository do pacote repository

// Marca a classe como um serviço do Spring para gerenciamento de detalhes do usuário
@Service // Define a classe UserDetailsServiceImpl como um serviço do Spring
public class UserDetailsServiceImpl implements UserDetailsService { // Define a classe UserDetailsServiceImpl que implementa a interface UserDetailsService

    @Autowired // Injeta automaticamente a dependência do UsuarioRepository
    private UsuarioRepository usuarioRepository; // Declara o repositório de usuários

    @Override // Sobrescreve o método loadUserByUsername da interface UserDetailsService
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Método para carregar os detalhes do usuário pelo nome de usuário

        if (username == null || username.trim().isEmpty()) { // Verifica se o nome de usuário é nulo ou vazio
            throw new UsernameNotFoundException("Usuário (e-mail) não pode ser vazio"); // Lança uma exceção se o nome de usuário for inválido
        }

        Optional<Usuario> usuario = usuarioRepository.findByUsuario(username); // Busca o usuário no repositório pelo nome de usuário

        if (usuario.isPresent()) { // Verifica se o usuário foi encontrado
            return new UserDetailsImpl(usuario.get()); // Retorna os detalhes do usuário encapsulados em um objeto UserDetailsImpl
        } else { // Se o usuário não for encontrado
            throw new UsernameNotFoundException("Usuário não encontrado: " + username); // Lança uma exceção indicando que o usuário não foi encontrado
        }

    }
}