package com.generation.blogpessoal.security; // Define o pacote onde a classe está localizada

import java.io.IOException; // Importa a classe IOException para tratar exceções de entrada/saída
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação Autowired para injeção de dependências
import org.springframework.http.HttpStatus; // Importa a classe HttpStatus para representar códigos de status HTTP
import org.springframework.lang.NonNull; // Importa a anotação NonNull para indicar que um parâmetro não pode ser nulo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa a classe UsernamePasswordAuthenticationToken para representar tokens de autenticação
import org.springframework.security.core.context.SecurityContextHolder; // Importa a classe SecurityContextHolder para acessar o contexto de segurança atual
import org.springframework.security.core.userdetails.UserDetails; // Importa a interface UserDetails para representar os detalhes do usuário
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa a exceção UsernameNotFoundException para indicar que o usuário não foi encontrado
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Importa a classe WebAuthenticationDetailsSource para construir detalhes de autenticação web
import org.springframework.stereotype.Component; // Importa a anotação Component para marcar a classe como um componente do Spring
import org.springframework.web.filter.OncePerRequestFilter; // Importa a classe OncePerRequestFilter para criar filtros que são executados uma vez por requisição
import io.jsonwebtoken.ExpiredJwtException; // Importa a exceção ExpiredJwtException para indicar que o token JWT expirou
import io.jsonwebtoken.MalformedJwtException; // Importa a exceção MalformedJwtException para indicar que o token JWT está malformado
import io.jsonwebtoken.security.SignatureException; // Importa a exceção SignatureException para indicar que a assinatura do token JWT é inválida
import jakarta.servlet.FilterChain; // Importa a classe FilterChain para representar a cadeia de filtros
import jakarta.servlet.ServletException; // Importa a exceção ServletException para tratar erros de servlet
import jakarta.servlet.http.HttpServletRequest; // Importa a classe HttpServletRequest para representar requisições HTTP
import jakarta.servlet.http.HttpServletResponse; // Importa a classe HttpServletResponse para representar respostas HTTP


@Component //Marca a classe como um componente do Spring para atuar como um filtro de autenticação JWT para cada requisição HTTP
public class JwtAuthFilter extends OncePerRequestFilter { // Define a classe JwtAuthFilter que estende OncePerRequestFilter para garantir que o filtro seja executado uma vez por requisição

    // Injeção de dependência do serviço JWT e do serviço de detalhes do usuário
    @Autowired
    private JwtService jwtService; // Declara o serviço JWT

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Declara o serviço de detalhes do usuário

    // Sobrescreve o método doFilterInternal para implementar a lógica de autenticação JWT
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, // Sobrescreve o método doFilterInternal para implementar a lógica de autenticação JWT
                                    @NonNull HttpServletResponse response, // Sobrescreve o método doFilterInternal para implementar a lógica de autenticação JWT
                                    @NonNull FilterChain filterChain) throws ServletException, IOException { // Sobrescreve o método doFilterInternal para implementar a lógica de autenticação JWT

        try { // Início do bloco try para capturar exceções relacionadas ao JWT
            String token = extractTokenFromRequest(request); // Extrai o token JWT da requisição HTTP

            if (token == null || SecurityContextHolder.getContext().getAuthentication() != null) { // Verifica se o token é nulo ou se já existe uma autenticação no contexto de segurança
                filterChain.doFilter(request, response); // Continua a cadeia de filtros sem autenticação
                return; // Sai do método
            }

            processJwtAuthentication(request, token); // Processa a autenticação JWT
            filterChain.doFilter(request, response); // Continua a cadeia de filtros após a autenticação bem-sucedida

        } catch (ExpiredJwtException | SignatureException | MalformedJwtException // Captura exceções específicas relacionadas ao JWT
                | UsernameNotFoundException e) { // Captura exceções específicas relacionadas ao JWT
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // Define o status da resposta HTTP como 401 (Unauthorized)
        }
    }

    // Método auxiliar para extrair o token JWT do cabeçalho da requisição HTTP
    private String extractTokenFromRequest(HttpServletRequest request) { // Utilitário para extrair o token JWT do cabeçalho da requisição HTTP

        String authHeader = request.getHeader("Authorization"); // Obtém o valor do cabeçalho "Authorization" da requisição HTTP

        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) { // Verifica se o cabeçalho não é nulo, começa com "Bearer " e tem comprimento maior que 7
            return authHeader.substring(7); // Retorna o token JWT extraído do cabeçalho, removendo o prefixo "Bearer "
        }

        return null; // Retorna nulo se o token não estiver presente ou for inválido
    }

    // Método auxiliar para processar a autenticação JWT
    private void processJwtAuthentication(HttpServletRequest request, String token) { // Utilitário para processar a autenticação JWT

        String username = jwtService.extractUsername(token); // Extrai o nome de usuário do token JWT

        if (username != null && !username.trim().isEmpty()) { // Verifica se o nome de usuário não é nulo ou vazio
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Carrega os detalhes do usuário pelo nome de usuário

            if (jwtService.validateToken(token, userDetails)) { // Valida o token JWT em relação aos detalhes do usuário

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( // Cria um token de autenticação baseado em nome de usuário e senha
                        userDetails, null, userDetails.getAuthorities()); // Inicializa o token com os detalhes do usuário e suas autoridades

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Define os detalhes da autenticação com base na requisição HTTP

                SecurityContextHolder.getContext().setAuthentication(authToken); // Define o token de autenticação no contexto de segurança atual

            } else { // Se o token JWT for inválido
                throw new RuntimeException("Token JWT inválido ou expirado"); // Lança uma exceção indicando que o token JWT é inválido ou expirado
            }

        } else { // Se o nome de usuário não puder ser extraído do token JWT
            throw new RuntimeException("Usuário não pode ser extraído do token JWT"); // Lança uma exceção indicando que o usuário não pode ser extraído do token JWT
        }
    }

}