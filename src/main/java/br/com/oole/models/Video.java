package br.com.oole.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.oole.dto.OlheiroDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(exclude = {"olheirosLikes"})
public class Video implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String title;
	
	private String descricao;
	
	private String url;
	
	private Date dataUpload;
	
	@ManyToMany(mappedBy = "videosLiked")
	@JsonIgnore
	private Set<Olheiro> olheirosLikes = new HashSet<Olheiro>();
	
	@ManyToOne
//	@JsonIgnore
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
	
	public List<OlheiroDTO> getLikes () {
		List<OlheiroDTO> aux = new ArrayList<OlheiroDTO>();
		for (Olheiro olheiro : olheirosLikes) {
			aux.add(Olheiro.toDTO(olheiro));
		}
		return aux;
	}

}
