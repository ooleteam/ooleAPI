package br.com.oole.resources;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.oole.models.Video;
import br.com.oole.services.VideoService;

@RestController
@RequestMapping(value = "/videos")
public class VideoResource {
	
	@Autowired
	private VideoService videoService;
	
	@GetMapping
	public ResponseEntity<List<Video>> findAll() {
		List<Video> list = videoService.findAll();  
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Video> find(@PathVariable Integer id) {
		Video obj = videoService.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/by-player/{id}")
	public ResponseEntity<List<Video>> findByJogador(@PathVariable Integer id) {
		List<Video> obj = videoService.findByJogador(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@GetMapping(value = "/feed/{id}")
	public ResponseEntity<List<Video>> feedByJogador(
			@PathVariable Integer id,
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="orderBy", defaultValue="jogadoresSeguidores") String orderBy, 
			@RequestParam(value="direction", defaultValue="DESC") String direction) {
		List<Video> obj = videoService.feedByJogador(id, page, 24, "dataUpload", "DESC");
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<Void> uploadVideo(
			@RequestParam(name="file") MultipartFile file, 
			@RequestParam(name="title") String title,
			@RequestParam(name="desc") String desc,
			@RequestParam(name ="id") Integer id) {
		URI uri = videoService.uploadVideos(file, id, title, desc);
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/like")
	public ResponseEntity<Void> like(
			@RequestParam(name="idUser") Integer idUser, 
			@RequestParam(name="idVideo") Integer idVideo) {
		videoService.likeVideo(idVideo, idUser);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value="/dislike")
	public ResponseEntity<Void> dislike(
			@RequestParam(name="idUser") Integer idUser, 
			@RequestParam(name="idVideo") Integer idVideo) {
		videoService.dislikeVideo(idVideo, idUser);
		return ResponseEntity.noContent().build();
	}
	
//	@PutMapping(value="/{idUser}/videos/{idVideo}")
//	public ResponseEntity<Void> uploadVideo(
//			@RequestParam(name="file") MultipartFile file, 
//			@RequestParam(name="title") String title,
//			@RequestParam(name="desc") String desc,
//			@PathVariable(name ="id") Integer id) {
//		URI uri = videoService.uploadVideos(file, id, title, desc);
//		return ResponseEntity.created(uri).build();
//	}
	
}
