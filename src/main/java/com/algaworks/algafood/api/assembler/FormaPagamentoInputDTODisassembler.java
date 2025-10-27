package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.FormaPagamentoInputDTO;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoInputDTODisassembler {
	
	@Autowired
	private ModelMapper mapper;
	
	public FormaPagamento toDomainObcjet(FormaPagamentoInputDTO formaPagamentoInputDTO) {
		
		return mapper.map(formaPagamentoInputDTO, FormaPagamento.class);
	}
	
	public void copyToDomainObject(FormaPagamentoInputDTO formaPagamentoInputDTO, FormaPagamento formaPAgamento) {
		
		mapper.map(formaPagamentoInputDTO, formaPAgamento);
	}
	
}
