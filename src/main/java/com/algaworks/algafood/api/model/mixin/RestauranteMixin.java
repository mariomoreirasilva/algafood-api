package com.algaworks.algafood.api.model.mixin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class RestauranteMixin {
	//no put por exemplo, não vai atualizar o nome da cozinha no http do restaurante. Se colocar no Json o campo nome da cozinha retorna mensagem de erro
		// se não colocar, não fazia nada, nem mensagem de erro e nem atualização. Mas pode confundir o consumidor da API
		//colocando o value, .... e allowGetters = true so faz na descerilização(objeto para json(no get por exemplo) e no put não aparece e se colocar o nome da erro)		
	@JsonIgnoreProperties(value = {"nome"} , allowGetters = true) 
	private Cozinha cozinha;
	
	@JsonIgnore
	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	private LocalDateTime dataAtualizacao;
	
	@JsonIgnore
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>();
	
}
