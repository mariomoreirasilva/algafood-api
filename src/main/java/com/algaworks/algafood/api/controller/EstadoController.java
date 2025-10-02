package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;
	
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}
	
//	@GetMapping("/{estadoId}")
//	public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
//		Optional<Estado> estado = estadoRepository.findById(estadoId);
//		
//		if (estado.isPresent()) {
//			return ResponseEntity.ok(estado.get());
//		}
//		
//		return ResponseEntity.notFound().build();
		
//	}
	
	@GetMapping("/{estadoId}")
	public Estado buscar(@PathVariable Long estadoId) {
		
		System.out.println("Passei por aqui.");
		return cadastroEstado.buscarOuFalha(estadoId);
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar(@RequestBody @Valid Estado estado) {
		return cadastroEstado.salvar(estado);
	}
	
//	@PutMapping("/{estadoId}")
//	public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId,
//			@RequestBody Estado estado) {
//		Estado estadoAtual = estadoRepository.findById(estadoId).orElse(null);
//		
//		if (estadoAtual != null) {
//			BeanUtils.copyProperties(estado, estadoAtual, "id");
//			
//			estadoAtual = cadastroEstado.salvar(estadoAtual);
//			return ResponseEntity.ok(estadoAtual);
//		}
//		
//		return ResponseEntity.notFound().build();
//	}
	
	@PutMapping("/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId,
			@RequestBody @Valid Estado estado){
		
		Estado estadoAtual = cadastroEstado.buscarOuFalha(estadoId);
		BeanUtils.copyProperties(estado, estadoAtual, "id");
		return cadastroEstado.salvar(estadoAtual);
	}
	
//	@DeleteMapping("/{estadoId}")
//	public ResponseEntity<?> remover(@PathVariable Long estadoId) {
//		try {
//			cadastroEstado.excluir(estadoId);	
//			return ResponseEntity.noContent().build();
//			
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.notFound().build();
//			
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//					.body(e.getMessage());
//		}
//	}
	
	 @DeleteMapping("/{estadoId}")
	 public void remover(@PathVariable Long estadoId) {
		  cadastroEstado.excluir(estadoId);
	 }
	
}
