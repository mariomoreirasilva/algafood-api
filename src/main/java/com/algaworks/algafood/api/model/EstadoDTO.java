package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoDTO {
	private Long id;
	private String nome;

}
