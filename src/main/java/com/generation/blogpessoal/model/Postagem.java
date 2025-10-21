package com.generation.blogpessoal.model; // Define o pacote onde a classe está localizada

import java.time.LocalDateTime; // Importa a classe para trabalhar com data e hora

import org.hibernate.annotations.UpdateTimestamp; // Importa a anotação para atualizar o timestamp automaticamente

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importa a anotação para evitar problemas de serialização JSON

import jakarta.persistence.Column; // Importa a anotação para definir propriedades da coluna no banco de dados
import jakarta.persistence.Entity; // Importa a anotação para definir uma entidade JPA
import jakarta.persistence.GeneratedValue; // Importa a anotação para definir a estratégia de geração de valores
import jakarta.persistence.GenerationType; // Importa a enumeração para estratégias de geração de valores
import jakarta.persistence.Id; // Importa a anotação para definir a chave primária
import jakarta.persistence.ManyToOne; // Importa a anotação para definir um relacionamento muitos-para-um
import jakarta.persistence.Table; // Importa a anotação para definir o nome da tabela no banco de dados
import jakarta.validation.constraints.NotBlank; // Importa a anotação para validação de campos não vazios
import jakarta.validation.constraints.Size; // Importa a anotação para validação de tamanho de campos

@Entity // inidca ao spring que essa classe é uma entidade (tabela)
@Table(name = "tb_postagens") // Define o nome da tabela que sera criada no banco de dados
public class Postagem { // Define a classe Postagem

	@Id // PRIMARY KEY(id) // indica que o atributo id é a chave primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT, quem cuida disso é o DB
	private Long id;

	// Validação dos dados
	@Column(length = 100) // Define o tamanho máximo do campo no banco de dados
	@NotBlank(message = "O atributo Título é obrigatório!") // Impedir que o título seja em branco
	@Size(min = 5, max = 100, message = "O atributo Título deve conter no mínimo 5 e no máximo 100 caracteres") // Define o tamanho mínimo e máximo do campo
	private String titulo; // Define o atributo titulo

	@Column(length = 1000) // Define o tamanho máximo do campo no banco de dados
	@NotBlank(message = "O atributo Texto é obrigatório!") // Impedir que o Texto seja em branco
	@Size(min = 10, max = 1000, message = "O atributo Texto deve conter no mínimo 10 e no máximo 1000 caracteres") // Define o tamanho mínimo e máximo do campo
	private String texto; // Define o atributo texto

	@UpdateTimestamp // Atualiza a data e hora automaticamente sempre que houver uma atualização na
						// postagem
	private LocalDateTime data; // Define o atributo data como LocalDateTime // Guarda tanto a data quanto a
								// hora

	@ManyToOne // Define o relacionamento um-para-muitos com Postagem)
	@JsonIgnoreProperties("postagem")
	private Tema tema; // Define o atributo postagem como uma lista de Postagem

	@ManyToOne // Define o relacionamento um-para-muitos com Postagem)
	@JsonIgnoreProperties("postagem")// Evita o loop infinito na serialização JSON
	private Usuario usuario; // Define o atributo postagem como uma lista de Postagem

	// Métodos Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}