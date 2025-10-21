package com.generation.blogpessoal.security; // Define o pacote onde a classe está localizada

import java.util.Collection; // Importa a interface Collection para representar uma coleção de objetos
import java.util.Collections; // Importa a classe Collections para trabalhar com coleções

import org.springframework.security.core.GrantedAuthority; // Importa a interface GrantedAuthority para representar autoridades concedidas a um usuário
import org.springframework.security.core.userdetails.UserDetails; // Importa a interface UserDetails para representar os detalhes do usuário

import com.generation.blogpessoal.model.Usuario; // Importa a classe Usuario do pacote model

public class UserDetailsImpl implements UserDetails { // Define a classe UserDetailsImpl que implementa a interface UserDetails

    private static final long serialVersionUID = 1L; // Declara uma constante serialVersionUID para a serialização da classe

    private String username; // Declara o campo username para armazenar o nome de usuário
    private String password; // Declara o campo password para armazenar a senha do usuário 

    public UserDetailsImpl(Usuario user) { // Construtor que recebe um objeto Usuario
        this.username = user.getUsuario(); // Inicializa o campo username com o nome de usuário do objeto Usuario
        this.password = user.getSenha(); // Inicializa o campo password com a senha do objeto Usuario
    }

    // Implementação dos métodos da interface UserDetails 
    @Override // Sobrescreve o método getAuthorities da interface UserDetails 
    public Collection<? extends GrantedAuthority> getAuthorities() { // Retorna uma coleção vazia de autoridades concedidas ao usuário

        return Collections.emptyList(); // Retorna uma coleção vazia de autoridades concedidas ao usuário
    }

    // Implementação dos métodos da interface UserDetails para obter informações do usuário e estado da conta
    @Override
    public String getPassword() {

        return password;
    }
    
    @Override
    public String getUsername() {

        return username;
    }

    // Implementação dos métodos da interface UserDetails para verificar o estado da conta do usuário 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}