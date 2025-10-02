package com.algaworks.algafood.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível."),
	RECURSO_NAO_ENCONTRADO("/recuso-nao-encontrata", "REcurso não encontrado."),
	NEGOCIO_NAO_EXISTE("/requisicao-campo-invalido","Requisição inválida."),
	ENTIDADE_EM_USO("/entidade-em-uso", "Erro de integridade referencial. Entidade em uso."),
	MEDIATYPE_NAO_SUPORTADO("/mediatype-nao-suportado", "Tipo de MediaType não suportado."),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro informado inválido."),
	ERRO_DE_SISTEMA("/erro-nao-tratado", "Erro de sistema."),
	DADO_INVALIDO("/dado-invalido", "Dado inválido ou faltante.");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title){
		this.uri = ("https://desban.org.br") + path;
		this.title = title;
		
	}

}
