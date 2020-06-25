package br.com.oole.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class OlheiroDTO {
//	public OlheiroDTO(Integer id2, String urlFotoPerfil2, String nome2, String dataNascimento2, String sexo2,
//			String login2, String email2, String telefone2, String nacionalidade2, String cep2, String bairro2,
//			String cidade2, String endereco2, String estado2) {
//		// TODO Auto-generated constructor stub
//	}

	private Integer id;
	private String urlFotoPerfil;
	private String nome;
	private String dataNascimento;
	
	private String sexo;
	
	private String login;
	
	private String email;
	
	private String telefone;
	
	private String nacionalidade;
	
	private String cep;
	
	private String bairro;
	
	private String cidade;
	
	private String endereco;
	
	private String estado;
}
