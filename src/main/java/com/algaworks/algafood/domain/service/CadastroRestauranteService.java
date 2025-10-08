package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
	private static final String MSG_COZINHA_NAO_ENCONTADA = "Não existe cadastro de cozinha com código %d";

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cozinhaService;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		
		Long cozinhaId = restaurante.getCozinha().getId();
		
//		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
//			.orElseThrow(() -> new EntidadeNaoEncontradaException(
//					String.format(MSG_COZINHA_NAO_ENCONTADA, cozinhaId)));
		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);
		
		restaurante.setCozinha(cozinha);		
		
		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscarOuFalha(Long restauranteId) {
		
		return restauranteRepository.findById(restauranteId).
				orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
		
	}
	
}
