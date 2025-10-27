package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CidadeDTO;
import com.algaworks.algafood.api.model.input.CidadeInputDTO;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeInputDTODisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Cidade toDomainObcjet(CidadeInputDTO cidadeInputDTO) {
		
		return modelMapper.map(cidadeInputDTO, Cidade.class);
		
	}
	
	public void copyToDomainObject(CidadeInputDTO cidadeInputDTO, Cidade cidade) {
		
		modelMapper.map(cidadeInputDTO, cidade);
	}

}
