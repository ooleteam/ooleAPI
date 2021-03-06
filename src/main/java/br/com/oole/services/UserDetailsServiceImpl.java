package br.com.oole.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.oole.DAO.JogadorDAO;
import br.com.oole.DAO.OlheiroDAO;
import br.com.oole.models.Jogador;
import br.com.oole.models.Olheiro;
import br.com.oole.models.enums.Perfil;
import br.com.oole.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private JogadorDAO jogadordao;
	
	@Autowired
	private OlheiroDAO olheirodao;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Jogador j = jogadordao.findByEmail(email);
		
		if (j == null) {
			Olheiro o = olheirodao.findByEmail(email);
			if(o == null) {
				throw new UsernameNotFoundException(email);
			}
			
			Set<Perfil> perfis = new HashSet<Perfil>();
			perfis.add(o.getPerfil());
			
			return new UserSS(o.getId(), o.getEmail(), o.getSenha(), perfis);
		}
		
		Set<Perfil> perfis = new HashSet<Perfil>();
		perfis.add(j.getPerfil());
		
		return new UserSS(j.getId(), j.getEmail(), j.getSenha(), perfis);
	}
}
