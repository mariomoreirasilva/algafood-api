package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDTODisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toDomainObjetc(RestauranteInputDTO restauranteInputDTO) {
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
		return modelMapper.map(restauranteInputDTO, Restaurante.class);
	}
	
	public void copyToDomainObject(RestauranteInputDTO restauranteInputDTO, Restaurante restaurante) {
		//para evitar erro 500
		//identifier of an instance of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 3
		Cozinha cozinha = new Cozinha();
		restaurante.setCozinha(cozinha);		
		
		//diferente do método acima pois não cria uma nova instancia de restaurante. O restaurante já foi passado.
		//Acima é criado uma nova instancia, new.....
		modelMapper.map(restauranteInputDTO, restaurante);
		
	}
}
