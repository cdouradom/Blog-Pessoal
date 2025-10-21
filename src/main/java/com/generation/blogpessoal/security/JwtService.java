package com.generation.blogpessoal.security; // Define o pacote onde a classe está localizada

import java.time.Duration; // Importa a classe Duration para representar uma duração de tempo
import java.time.Instant; // Importa a classe Instant para representar um ponto no tempo
import java.util.Date; // Importa a classe Date para representar datas
import javax.crypto.SecretKey; // Importa a interface SecretKey para representar uma chave secreta de criptografia
import org.springframework.security.core.userdetails.UserDetails; // Importa a interface UserDetails para representar os detalhes do usuário
import org.springframework.stereotype.Component; // Importa a anotação Component para marcar a classe como um componente do Spring
import io.jsonwebtoken.Claims; // Importa a interface Claims para representar as reivindicações de um token JWT
import io.jsonwebtoken.Jwts; // Importa a classe Jwts para criar e analisar tokens JWT
import io.jsonwebtoken.io.Decoders; // Importa a classe Decoders para decodificar strings codificadas
import io.jsonwebtoken.security.Keys; // Importa a classe Keys para gerar chaves de criptografia

@Component // Marca a classe como um componente do Spring para gerenciamento de tokens JWT
public class JwtService { // Define a classe JwtService para manipulação de tokens JWT

    // Metadados do token JWT
    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"; // Chave secreta usada para assinar os tokens JWT 
    private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(60); // Duração de expiração do token JWT (60 minutos)
    private final SecretKey signingKey; // Chave secreta usada para assinar os tokens JWT

    // Construtor que inicializa a chave de assinatura a partir da chave secreta codificada em Base64
    public JwtService() { // Construtor que inicializa a chave de assinatura a partir da chave secreta codificada em Base64
        byte[] keyBytes = Decoders.BASE64.decode(SECRET); // Decodifica a chave secreta de uma string Base64 para um array de bytes
        this.signingKey = Keys.hmacShaKeyFor(keyBytes); // Gera a chave de assinatura HMAC-SHA a partir dos bytes decodificados
    }

    // Método auxiliar para extrair todas as reivindicações de um token JWT
    private Claims extractAllClaims(String token) { // Utilitário para extrair todas as reivindicações de um token JWT
        return Jwts.parser() // Inicia o parser de tokens JWT
                .verifyWith(signingKey) // Configura a chave de assinatura para verificar o token JWT
                .build() // Constrói o parser de tokens JWT
                .parseSignedClaims(token) // Analisa o token JWT assinado e retorna as reivindicações
                .getPayload(); // Retorna as reivindicações extraídas do token JWT
    }

    // Método para extrair o nome de usuário (subject) de um token JWT
    public String extractUsername(String token) { // Utilitário para extrair o nome de usuário (subject) de um token JWT
        return extractAllClaims(token).getSubject(); // Retorna o nome de usuário extraído das reivindicações do token JWT
    }

    // Método para extrair a data de expiração de um token JWT
    public Date extractExpiration(String token) { // Utilitário para extrair a data de expiração de um token JWT
        return extractAllClaims(token).getExpiration(); // Retorna a data de expiração extraída das reivindicações do token JWT
    }

    // Método para validar um token JWT em relação aos detalhes do usuário
    public boolean validateToken(String token, UserDetails userDetails) { // Utilitário para validar um token JWT em relação aos detalhes do usuário
        Claims claims = extractAllClaims(token); // Extrai todas as reivindicações do token JWT
        return claims.getSubject().equals(userDetails.getUsername()) && // Verifica se o nome de usuário extraído do token corresponde ao nome de usuário dos detalhes do usuário
                claims.getExpiration().after(new Date()); // Verifica se o token JWT não expirou comparando a data de expiração com a data atual
    }

    // Método para gerar um token JWT para um determinado nome de usuário
    public String generateToken(String username) { // Utilitário para gerar um token JWT para um determinado nome de usuário
        Instant now = Instant.now(); // Obtém o instante atual
        return Jwts.builder() // Inicia a construção do token JWT
                .subject(username) // Define o nome de usuário (subject) do token JWT
                .issuedAt(Date.from(now)) // Define a data de emissão do token JWT
                .expiration(Date.from(now.plus(EXPIRATION_DURATION))) // Define a data de expiração do token JWT
                .signWith(signingKey) // Assina o token JWT com a chave de assinatura
                .compact(); // Constrói e retorna o token JWT como uma string compacta
    }
}