package br.com.oole.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.licensemanager.model.AuthorizationException;

import br.com.oole.DAO.VideoDAO;
import br.com.oole.models.Jogador;
import br.com.oole.models.Olheiro;
import br.com.oole.models.Video;
import br.com.oole.models.enums.Perfil;
import br.com.oole.security.UserSS;
import br.com.oole.services.exceptions.FileException;
import br.com.oole.services.exceptions.ObjectNotFoundException;

@Service
public class VideoService {
	
	@Autowired
	private VideoDAO dao;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private JogadorService jogadorService;
	
	@Autowired
	private OlheiroService olheiroService;
	
	public Video insert(Video obj) {
//		dao.insert(obj);
		return dao.save(obj);
	}
	
	public List<Video> findAll() {
		return dao.findAll();
	}
	
	public Video find(Integer id) {
		Optional<Video> obj = dao.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Video.class.getName()));
	}
	
	public List<Video> findByJogador(Integer jogadorId){
		return dao.findByJogador(jogadorId);
	}
	
	public List<Video> feedByJogador(Integer idUser,Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		
		if(user.hasRole(Perfil.OLHEIRO)) {
			Olheiro olheiro = olheiroService.find(idUser);
			PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
			
			List<Video> aux = new ArrayList<Video>();
			for (Jogador j : olheiro.getJogadores()) {
				aux.addAll((dao.feed(j.getId())));
			}
			
			
			return aux;
		}
		
		Jogador jogador = jogadorService.find(idUser);
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Video> aux = new ArrayList<Video>();
		for (Jogador j : jogador.getJogadoresSeguindo()) {
			aux.addAll((dao.feed(j.getId())));
		}
		
		
		return aux;
		
//		List<Video> feedVideos
//		for (Jogador seguindo : jogador.getJogadoresSeguindo()) {
//			seguindo.getVideos();
//		}
//		return dao.findByJogador(jogadorId);
	}
	
	public void likeVideo(Integer idVideo, Integer idUser) {
		
		Video video = find(idVideo);
		
		Olheiro olheiro = olheiroService.find(idUser);
		if(video.getOlheirosLikes().contains(olheiro)) {
			video.getOlheirosLikes().remove(olheiro);
			olheiro.getVideosLiked().remove(video);
		}else {
			video.getOlheirosLikes().add(olheiro);
			olheiro.getVideosLiked().add(video);
		}
		
//		if(user.getAuthorities().toString().contains("JOGADOR")) {
//			Jogador jogador = jogadorService.find(idUser);
//			
//			if(video.getJogadorLike().contains(jogador)) {
//				video.getJogadorLike().remove(jogador);
//			}else {
//				video.getJogadorLike().add(jogador);
//				if(video.getJogadorDislike().contains(jogador)) {
//					video.getJogadorDislike().remove(jogador);
//				}
//			}
//		}else{
//			Olheiro olheiro = olheiroService.find(idUser);
//			if(video.getOlheiroLike().contains(olheiro)) {
//				video.getOlheiroLike().remove(olheiro);
//			}else {
//				video.getOlheiroLike().add(olheiro);
//				if(video.getOlheiroDislike().contains(olheiro)) {
//					video.getOlheiroDislike().remove(olheiro);
//				}
//			}
//		}
		
		dao.save(video);
		
	}
	
//	public void dislikeVideo(Integer idVideo, Integer idUser) {
//		
//		UserSS user = UserService.authenticated();
//		if (user==null || !user.hasRole(Perfil.JOGADOR) && !idUser.equals(user.getId())) {
//			throw new AuthorizationException("Acesso negado");
//		}
//		
//		Video video = find(idVideo);
//		
//		if(user.getAuthorities().toString().contains("JOGADOR")) {
//			Jogador jogador = jogadorService.find(idUser);
//			
//			if(video.getJogadorDislike().contains(jogador)) {
//				video.getJogadorDislike().remove(jogador);
//			}else {
//				video.getJogadorDislike().add(jogador);
//				if(video.getJogadorLike().contains(jogador)) {
//					video.getJogadorLike().remove(jogador);
//				}
//			}
//		}else{
//			Olheiro olheiro = olheiroService.find(idUser);
//			if(video.getOlheiroDislike().contains(olheiro)) {
//				video.getOlheiroDislike().remove(olheiro);
//			}else {
//				video.getOlheiroDislike().add(olheiro);
//				if(video.getOlheiroLike().contains(olheiro)) {
//					video.getOlheiroLike().remove(olheiro);
//				}
//			}
//		}
//		
//		dao.save(video);
//		
//	}
	
	public URI uploadVideos(MultipartFile file, Integer id, String title, String desc){
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.JOGADOR) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Jogador newObj = jogadorService.find(id);
		Date data = new Date();
		String fileName = title.trim() + ".mp4";
		System.out.println(fileName);
		URL url = s3Service.uploadVideo(getInputStream(file, "mp4"), fileName, id);
		
		Video video = new Video(null,title,desc,url.toString(), data, newObj);
		System.out.println(video.getTitle());
//		newObj.getVideos().add(video);
		System.out.println("Add to jogadador");
		insert(video);
		System.out.println("Foisasasasjbjbhjsabhbjdshadshsdbhjhjbasdsadsajbdjasdjasjdadbs");
		deleteFromlLocal(file.getOriginalFilename());
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro de IO: " + e.getMessage());
		}
	}	
	
	private void deleteFromlLocal(String name) {
		File myObj = new File(name);
		myObj.delete();
	}
	
	private File getInputStream(MultipartFile file, String extension) {
		try {
			File convFile = new File(file.getOriginalFilename());
	        FileOutputStream fos = new FileOutputStream(convFile);
	        fos.write(file.getBytes());
	        fos.close();
	        return convFile;
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
		
	}
}
