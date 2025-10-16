package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity //inidica ao spring que essa classe é uma entidade (tabela)
@Table(name = "tb_temas") // Define o nome da tabela que sera criada no banco de dados
public class Tema {
	
	@Id  // PRIMARY KEY(id) // indica que o atributo id é a chave primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT, quem cuida disso é o DB
	private Long id; 
		
	@Column(length = 1000) // Define o tamanho máximo do campo no banco de dados
	@NotBlank(message = "O atributo Descricao é obrigatório!") // Impedir que a descricao seja em branco
	@Size(min = 10, max = 100, message = "O atributo Descricao deve conter no mínimo 10 e no máximo 100 caracteres") // Define o tamanho mínimo e máximo do campo
	private String descricao; // Define o atributo descricao
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // Define o relacionamento um-para-muitos com Postagem)
	@JsonIgnoreProperties(value = "tema", allowSetters = true)
	private List<Postagem> postagem; // Define o atributo postagem como uma lista de Postagem

	// Métodos Getters e Setters
	public Long getId() { // Getter do id
		return id; // Retorna o valor do id
	}

	public void setId(Long id) { // Setter do id
		this.id = id; // Atribui o valor ao id
	}

	public String getDescricao() { // Getter do descricao
		return descricao; // Retorna o valor do descricao
	}

	public void setTexto(String descricao) { // Setter do descricao
		this.descricao = descricao; // Atribui o valor ao descricao
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	
}
