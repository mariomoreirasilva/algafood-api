

package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.EstadoInputDTO;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoInputDTODisassembler {
	
	@Autowired
	ModelMapper modelMapper;
	
	public Estado toDomainObjetc(EstadoInputDTO estadoInputDTO) {
		
		return modelMapper.map(estadoInputDTO, Estado.class);
	}
	
	public void copyToDomainObject(EstadoInputDTO estadoInputDTO, Estado estado) {
		
		modelMapper.map(estadoInputDTO, estado);
		
	}

}
