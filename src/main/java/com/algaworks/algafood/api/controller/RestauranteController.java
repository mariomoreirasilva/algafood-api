package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteDTOAssembler;
import com.algaworks.algafood.api.assembler.RestauranteInputDTODisassembler;
import com.algaworks.algafood.api.model.RestauranteDTO;
import com.algaworks.algafood.api.model.input.CozinhaIdInputDTO;
import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteDTOAssembler restauranteDTOAssembler;
	
	@Autowired
	private RestauranteInputDTODisassembler restauranteInputDTODisassembler;
	
	@GetMapping
	public List<RestauranteDTO> listar() {
//		//return restauranteRepository.findAll();
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteDTO> restaurantesDTO = restaurantes.stream().map(restaurante -> toDTO(restaurante))
//				.collect(Collectors.toList());
//		return restaurantesDTO;
		return restauranteDTOAssembler.toListDTO(restauranteRepository.findAll());
	
	}
	
	
	@GetMapping("/{restauranteId}")
	public RestauranteDTO buscar(@PathVariable Long restauranteId) {
		
		Restaurante restaurante = cadastroRestaurante.buscarOuFalha(restauranteId);		
		//RestauranteDTO restauranteDTO = toDTO(restaurante);		
		return restauranteDTOAssembler.toDTO(restaurante);
	}

	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid RestauranteInputDTO restauranteInputDTO) {
		try {
			Restaurante restaurante = restauranteInputDTODisassembler.toDomainObjetc(restauranteInputDTO);
			
			return restauranteDTOAssembler.toDTO(cadastroRestaurante.salvar(restaurante));
		}catch(EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteDTO atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInputDTO restauranteInputDTO) {
		
		try {
			
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalha(restauranteId);
			
			restauranteInputDTODisassembler.copyToDomainObject(restauranteInputDTO, restauranteAtual);
			
			//BeanUtils.copyProperties(restaurante, restauranteAtual,"id", "formasPagamento", "endereco", "dataCadastro", "produtos");
	
			return restauranteDTOAssembler.toDTO(cadastroRestaurante.salvar(restauranteAtual));
		}catch (EntidadeNaoEncontradaException e){
			throw new NegocioException(e.getMessage(), e);
		}
	}
	@PatchMapping("/{restauranteId}")
	public RestauranteDTO atualizarParcial(@PathVariable Long restauranteId,
	        @RequestBody Map<String, Object> campos, HttpServletRequest request) {
	    Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalha(restauranteId);
	    
	    merge(campos, restauranteAtual, request);
	    
	    return atualizar(restauranteId, toInputDTO(restauranteAtual));
	}
	
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, 
			HttpServletRequest request) {
		
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);//chamar a apiException handleHttpMessageNotReadable
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
	//			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		}catch(IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.desativar(restauranteId);
	}

	
	
	
//	private Restaurante toDomainObjetc(RestauranteInputDTO restauranteInputDTO) {
//		Restaurante restaurante = new Restaurante();
//		
//		restaurante.setNome(restauranteInputDTO.getNome());
//		restaurante.setTaxaFrete(restauranteInputDTO.getTaxaFrete());
//		
//		Cozinha cozinha = new Cozinha();
//		cozinha.setId(restauranteInputDTO.getCozinha().getId());
//		
//		restaurante.setCozinha(cozinha);
//		
//		return restaurante;
//	}
	
	private RestauranteInputDTO toInputDTO(Restaurante restaurante) {
		
		RestauranteInputDTO restauranteInputDTO = new RestauranteInputDTO();
		restauranteInputDTO.setNome(restaurante.getNome());
		restauranteInputDTO.setTaxaFrete(restaurante.getTaxaFrete());
		
		CozinhaIdInputDTO cozinhaIdInputDTO = new CozinhaIdInputDTO();
		cozinhaIdInputDTO.setId(restaurante.getCozinha().getId());
		
		restauranteInputDTO.setCozinha(cozinhaIdInputDTO);
		
		return restauranteInputDTO;
		
	}

	
}
