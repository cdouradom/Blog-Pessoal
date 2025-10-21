package com.generation.blogpessoal.model; // Define o pacote onde a classe está localizada

// Classe modelo para representar os dados de login do usuário sem criar tabela no banco de dados e ser utilizada apenas para transferência de dados. Ou seja, um DTO (Data Transfer Object).
public class UsuarioLogin { // Define a classe UsuarioLogin 
    private Long id; // Declara o campo id do tipo Long
    private String nome; // Declara o campo nome do tipo String
    private String usuario; // Declara o campo usuario do tipo String
    private String senha; // Declara o campo senha do tipo String
    private String foto; // Declara o campo foto do tipo String
    private String token; // Declara o campo token do tipo String

    // Getters e Setters para os campos da classe
    public Long getId() { 
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}