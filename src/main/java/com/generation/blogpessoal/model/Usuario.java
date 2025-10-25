package com.generation.blogpessoal.model; //

import java.util.List; // Importa a classe List para manipular listas de objetos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importa a anotação JsonIgnoreProperties para evitar problemas de serialização JSON

import io.swagger.v3.oas.annotations.media.Schema; // 
import jakarta.persistence.CascadeType; // Importa a enumeração CascadeType para definir o comportamento de cascata nas operações de banco de dados
import jakarta.persistence.Entity; // Importa a anotação Entity para marcar a classe como uma entidade JPA
import jakarta.persistence.FetchType; // Importa a enumeração FetchType para definir o tipo de carregamento das associações
import jakarta.persistence.GeneratedValue; // Importa a anotação GeneratedValue para especificar a estratégia de geração de valores para a chave primária
import jakarta.persistence.GenerationType; // Importa a enumeração GenerationType para definir a estratégia de geração de valores
import jakarta.persistence.Id; // Importa a anotação Id para marcar o campo como chave primária
import jakarta.persistence.OneToMany; //
import jakarta.persistence.Table; // Importa a anotação Table para especificar o nome da tabela no banco de dados
import jakarta.validation.constraints.Email; // Importa a anotação Email para validar que o campo é um endereço de email válido
import jakarta.validation.constraints.NotBlank; //
import jakarta.validation.constraints.Size; // Importa a anotação Size para validar o tamanho de strings

@Entity // Indica que a classe é uma entidade JPA
@Table(name = "tb_usuarios") // Especifica o nome da tabela no banco de dados
public class Usuario { // Define a classe Usuario

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que o campo é a chave primária e que seu valor será gerado automaticamente pelo banco de dados
    private Long id; // Declara o campo id como chave primária do tipo Long

    @NotBlank(message = "O Atributo Nome é Obrigatório!") // Valida que o campo nome não pode estar em branco
    @Size(min = 2, max = 100, message = "O Nome deve conter no mínimo 2 e no máximo 100 caracteres") // Valida o tamanho do campo nome
    private String nome; // Declara o campo nome do tipo String

    @Schema(example = "email@email.com.br")
    @NotBlank(message = "O Atributo Usuário é Obrigatório!") // Valida que o campo usuario não pode estar em branco
    @Email(message = "O Atributo Usuário deve ser um email válido!") // Valida que o campo usuario deve ser um email válido
    private String usuario; // Declara o campo usuario do tipo String

    @NotBlank(message = "O Atributo Senha é Obrigatório!") // Valida que o campo senha não pode estar em branco
    @Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres") // Valida o tamanho mínimo do campo senha
    private String senha; // Declara o campo senha do tipo String

    @Size(max = 5000, message = "O link da foto não pode ser maior do que 5000 caracteres") // Valida o tamanho máximo do campo foto
    private String foto; // Declara o campo foto do tipo String

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.REMOVE) // Define um relacionamento um-para-muitos com a entidade Postagem
    @JsonIgnoreProperties(value = "usuario", allowSetters = true) // Evita problemas de serialização JSON ignorando a propriedade "usuario" na entidade Postagem
    private List<Postagem> postagem; // Declara o campo postagem como uma lista de objetos Postagem

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

    public List<Postagem> getPostagem() {
        return this.postagem;
    }

    public void setPostagem(List<Postagem> postagem) {
        this.postagem = postagem;
    }

}