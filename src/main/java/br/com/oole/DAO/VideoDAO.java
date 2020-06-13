package br.com.oole.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.oole.models.Video;

public interface VideoDAO extends JpaRepository<Video, Integer>{
	
	@Transactional(readOnly = true)
	@Query("Select distinct obj from Video obj where obj.jogador.id like :id")
	public List<Video> findByJogador(@Param("id") Integer id);

	
	@Transactional(readOnly = true)
	@Query("SELECT v FROM Video as v WHERE v.jogador.id = :id ")
	List<Video> feed(@Param("id") Integer id);
}
