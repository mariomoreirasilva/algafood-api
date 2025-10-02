package com.algaworks.algafood.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
//incluir somente se o valor da propriedade estiver diferete de null @JsonInclude
@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class Problem {	
	
	private Integer status;
	private String type;
	private String title;
	private String datail;
	
	private String userMassage;
	private LocalDateTime dataHora;
	private List<Field> fields;
	
	@Getter
	@Builder
	public static  class Field{
		private String fildName;
		private String userMassege;
	}
}
