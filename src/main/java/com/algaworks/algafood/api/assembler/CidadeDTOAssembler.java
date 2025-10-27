package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CidadeDTO;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeDTOAssembler {

	@Autowired
	ModelMapper modelMapper;
	
	public CidadeDTO toDTO(Cidade cidade) {
		
		return modelMapper.map(cidade, CidadeDTO.class);		
		
	}
	
	public List<CidadeDTO> toListDTO(List<Cidade> cidades){
		
		return cidades.stream().map(cidade -> toDTO(cidade))
				.collect(Collectors.toList());
		
	}
}
