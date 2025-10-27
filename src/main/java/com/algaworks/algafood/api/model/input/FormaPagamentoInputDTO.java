package com.algaworks.algafood.api.model.input;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoInputDTO {
	
	@NotBlank
	@Column(nullable = false)
	private String descricao;

}
