package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FormaPagamentoDTOAssembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoInputDTODisassembler;
import com.algaworks.algafood.api.model.FormaPagamentoDTO;
import com.algaworks.algafood.api.model.input.FormaPagamentoInputDTO;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formapagamentos")
public class FormaPagamentoController {
	
	@Autowired
	FormaPagamentoDTOAssembler formaPagamentoDTOAssembler;
	
	@Autowired
	FormaPagamentoRepository repository;
	
	@Autowired
	private CadastroFormaPagamentoService service;
	
	@Autowired
	private FormaPagamentoInputDTODisassembler dtoDisassembler;
	
	@GetMapping
	public List<FormaPagamentoDTO> listar(){
	  
		return formaPagamentoDTOAssembler.lisTODTO(repository.findAll());
		
	}
	
	@GetMapping("/{formaPagamentoId}")
	public FormaPagamentoDTO buscarPorId(@PathVariable Long formaPagamentoId) {
		
		return formaPagamentoDTOAssembler.toDTO(service.buscarOuFalhar(formaPagamentoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoDTO salvar(@RequestBody @Valid FormaPagamentoInputDTO inputDTO) {
		
		FormaPagamento formaPagamento = dtoDisassembler.toDomainObcjet(inputDTO);
		
		return formaPagamentoDTOAssembler.toDTO(service.salvar(formaPagamento));
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoDTO atualizar(@PathVariable Long formaPagamentoId,
						@RequestBody @Valid FormaPagamentoInputDTO inputDTO) {
		
		FormaPagamento formaAtual = service.buscarOuFalhar(formaPagamentoId);
		
		 dtoDisassembler.copyToDomainObject(inputDTO, formaAtual);
		 
		 return formaPagamentoDTOAssembler.toDTO(service.salvar(formaAtual));
		
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	public void excluir(@PathVariable Long formaPagamentoId) {
		
		service.excluir(formaPagamentoId);
	}
	
	

}
