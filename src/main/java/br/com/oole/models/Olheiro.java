package br.com.oole.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.oole.dto.JogadorDTO;
import br.com.oole.dto.OlheiroDTO;
import br.com.oole.models.enums.Perfil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(exclude = {"jogadores","olheirosSeguidores","olheirosSeguindo"})
public class Olheiro implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private String dataNascimento;
	
	@JsonIgnore
	private String cpf;
	private String sexo;
	
	private String login;
	
	@JsonIgnore
	private String senha;
	
	@Column(unique=true)
	private String email;
	
	private String telefone;
	
	private Perfil perfil;
	
	private String nacionalidade;
	
	private String cep;
	
	private String bairro;
	
	private String cidade;
	
	private String estado;
	
	private String endereco;
	
	private String urlFotoPerfil;
	
	@ManyToMany(mappedBy = "olheiros")
	@JsonIgnore
	private Set<Jogador> jogadores = new HashSet<Jogador>();
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	@JoinTable(name = "olheirosLikes")
	private Set<Video> videosLiked = new HashSet<Video>();
	
	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "OLHEIRO_SEGUINDO",
	joinColumns = @JoinColumn(name = "olheiro_id"),
    inverseJoinColumns = @JoinColumn(name = "seguido_id"))
	private Set<Olheiro> olheirosSeguindo = new HashSet<Olheiro>();
	
	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "OLHEIRO_SEGUIDORES",
	joinColumns = @JoinColumn(name = "olheiro_id"),
    inverseJoinColumns = @JoinColumn(name = "seguidor_id"))
	private Set<Olheiro> olheirosSeguidores = new HashSet<Olheiro>();

	public Olheiro(Integer id, String nome, String dataNascimento, String cpf, String sexo, String login, String senha,
			String email, String telefone, String nacionalidade, String cep, String bairro, String cidade,
			String estado, String endereco) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.cpf = cpf;
		this.sexo = sexo;
		this.login = login;
		this.senha = senha;
		this.email = email;
		this.telefone = telefone;
		this.nacionalidade = nacionalidade;
		this.cep = cep;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.endereco = endereco;
		this.perfil = Perfil.OLHEIRO;
	}
	
	public List<JogadorDTO> getObservados(){
		List<JogadorDTO> list = new ArrayList<JogadorDTO>();
		for (Jogador jogador : this.jogadores) {
			list.add(Jogador.toDTO(jogador));
		}
		return list;
	}
	
	public List<OlheiroDTO> getSeguidores(){
		List<OlheiroDTO> list = new ArrayList<OlheiroDTO>();
		for (Olheiro olheiro : this.olheirosSeguidores) {
			list.add(Olheiro.toDTO(olheiro));
		}
		return list;
	}
	
	public List<OlheiroDTO> getSeguindo(){
		List<OlheiroDTO> list = new ArrayList<OlheiroDTO>();
		for (Olheiro olheiro : this.olheirosSeguindo) {
			list.add(Olheiro.toDTO(olheiro));
		}
		return list;
	}
	
	
	public static OlheiroDTO toDTO(Olheiro ol) {
		return new OlheiroDTO(ol.getId(), ol.getUrlFotoPerfil(), ol.getNome(), ol.getDataNascimento(), ol.getSexo(), ol.getLogin(), ol.getEmail(), ol.getTelefone(), ol.getNacionalidade(), ol.getCep(), ol.getBairro(), ol.getCidade(), ol.getEndereco(), ol.getEstado());
	}

}
