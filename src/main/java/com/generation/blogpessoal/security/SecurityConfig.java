package com.generation.blogpessoal.security; // Define o pacote onde a classe está localizada

import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.context.annotation.Bean; // Importa a anotação Bean para definir métodos que produzem beans gerenciados pelo Spring
import org.springframework.context.annotation.Configuration; // Importa a anotação Configuration para marcar a classe como uma configuração do Spring
import org.springframework.http.HttpMethod; // Importa a enumeração HttpMethod para representar métodos HTTP
import org.springframework.security.authentication.AuthenticationManager; // Importa a classe AuthenticationManager para gerenciar autenticações
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importa a classe AuthenticationConfiguration para configuração de autenticação
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importa a classe HttpSecurity para configurar a segurança HTTP
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importa a anotação EnableWebSecurity para habilitar a segurança web no aplicativo Spring
import org.springframework.security.config.http.SessionCreationPolicy; // Importa a enumeração SessionCreationPolicy para definir políticas de criação de sessão
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa a classe BCryptPasswordEncoder para codificação de senhas usando o algoritmo BCrypt
import org.springframework.security.crypto.password.PasswordEncoder; // Importa a interface PasswordEncoder para codificação de senhas
import org.springframework.security.web.SecurityFilterChain; // Importa a classe SecurityFilterChain para configurar a cadeia de filtros de segurança
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importa a classe UsernamePasswordAuthenticationFilter para filtrar autenticações baseadas em nome de usuário e senha

// Marca a classe como uma configuração do Spring Security para a aplicação e habilita a segurança web no aplicativo Spring para proteger endpoints e gerenciar autenticações e autorizações de usuários
@Configuration // Marca a classe como uma configuração do Spring Security para a aplicação
@EnableWebSecurity // Habilita a segurança web no aplicativo Spring para proteger endpoints e gerenciar autenticações e autorizações de usuários
public class SecurityConfig { // Define a classe SecurityConfig para configurar a segurança da aplicação

    private static final String[] PUBLIC_ENDPOINTS = { // Define um array de strings contendo os endpoints públicos que não requerem autenticação
            "/usuarios/logar", // Endpoint para login de usuários
            "/usuarios/cadastrar", // Endpoint para cadastro de novos usuários
            "/error/**", // Endpoint para tratamento de erros
            "/", "/docs", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**" // Endpoints relacionados à documentação da API
    };

    // Injeção de dependência do filtro de autenticação JWT 
    @Autowired // Injeta automaticamente a dependência do JwtAuthFilter
    private JwtAuthFilter jwtAuthFilter; // Declara o filtro de autenticação JWT

    // Definição dos beans necessários para a configuração de segurança da aplicação e autenticação de usuários por meio de JWT
    @Bean // Define o bean para o codificador de senhas usando o algoritmo BCrypt
    PasswordEncoder passwordEncoder() { // Método para criar um bean de PasswordEncoder
        return new BCryptPasswordEncoder(10); // Retorna uma instância de BCryptPasswordEncoder com força 10
    }
 
    @Bean // Define o bean para o gerenciador de autenticação 
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { // Método para criar um bean de AuthenticationManager
        return config.getAuthenticationManager(); // Retorna o gerenciador de autenticação a partir da configuração fornecida
    }

    @Bean // Define o bean para a cadeia de filtros de segurança HTTP
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Método para criar um bean de SecurityFilterChain
        return http // Configura a segurança HTTP 
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de criação de sessão como stateless (sem estado)
                .csrf(csrf -> csrf.disable()) // Desabilita a proteção CSRF (Cross-Site Request Forgery)
                .cors(cors -> { // Habilita o suporte a CORS (Cross-Origin Resource Sharing
                }) 
                .authorizeHttpRequests(auth -> auth // Configura as regras de autorização para requisições HTTP
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Permite acesso a todos os endpoints públicos definidos
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // Permite todas as requisições HTTP OPTIONS  
                        .anyRequest().authenticated()) // Exige autenticação para qualquer outra requisição
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro de autenticação JWT antes do filtro de autenticação padrão
                .build(); // Constrói e retorna a cadeia de filtros de segurança configurada
    }
}