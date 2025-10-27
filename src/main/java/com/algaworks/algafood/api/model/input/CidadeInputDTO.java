package com.algaworks.algafood.api.model.input;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.model.Estado;

@Component
public class CidadeInputDTO {

	@NotBlank
	@Column(nullable = false)
	private String nome;
		
	@Valid
	@NotNull	
	@JoinColumn(nullable = false)
	private Estado estado;
	
}
