package br.com.oole.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(exclude = {"jogadorLike","jogadorDislike","olheiroLike","olheiroDislike"})
public class Video implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String title;
	
	private String descricao;
	
	private String url;
	
	private Date dataUpload;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="Likes_Jogador")
	private Set<Jogador> jogadorLike = new HashSet<Jogador>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="dislikes_jogador")
	private Set<Jogador> jogadorDislike = new HashSet<Jogador>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="Likes_Olheiro")
	private Set<Olheiro> olheiroLike = new HashSet<Olheiro>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="dislikes_olheiro")
	private Set<Olheiro> olheiroDislike = new HashSet<Olheiro>();
	
	@ManyToOne
	@JsonIgnore
	private Jogador jogador;
	

	public Video(Integer id, String title, String descricao, String url, Date dataUpload, Jogador jogador) {
		super();
		this.id = id;
		this.title = title;
		this.descricao = descricao;
		this.url = url;
		this.dataUpload = dataUpload;
		this.jogador = jogador;
	}
	
	public int getTotalLikes () {
		return jogadorLike.size() + olheiroLike.size();
	}
	
	public int getTotalDislikes () {
		return jogadorDislike.size() + olheiroDislike.size();
	}

}
