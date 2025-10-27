package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.CozinhaInputDTO;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDTODisassembler {
	@Autowired
	private ModelMapper modelMapper;
	
	public Cozinha toDomainObjetc(CozinhaInputDTO cozinhaInputDTO) {
		
		return modelMapper.map(cozinhaInputDTO, Cozinha.class);		
	}
	
	public void copyToDomainObject(CozinhaInputDTO cozinhaInputDTO, Cozinha cozinha) {
		
		modelMapper.map(cozinhaInputDTO,cozinha);
		
	}
}
