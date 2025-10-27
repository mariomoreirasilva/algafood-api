package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoDTO;
import com.algaworks.algafood.domain.model.Endereco;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		
		//configuraçao do mapeamento. Abaixo um exemplo simples de mudança do nome. Fazendo isso
		//o modelmapper consegue mapear
//		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//		.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
		
		var modelMapper = new ModelMapper();
		
		var enderecoToEnderecoDTOTypeMap = modelMapper.createTypeMap(
				Endereco.class, EnderecoDTO.class);
		
		enderecoToEnderecoDTOTypeMap.<String>addMapping(
				enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
				(enderecoModelDest, vlrNomeEstado) -> enderecoModelDest.getCidade().setEstado(vlrNomeEstado));
		
		//o mapeamento pega a origem que é Endereco e o destino pega o campo e atribui o valor que vem do Endereco
		
		return modelMapper;
	}

}
