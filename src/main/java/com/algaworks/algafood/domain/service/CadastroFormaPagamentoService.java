package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class CadastroFormaPagamentoService {
	
	private static final String MSG_ENTIDADE_EM_USO = "Forma de pagamento com código %d não pode ser removido, pois está em uso";
	
	@Autowired
	private FormaPagamentoRepository repository;
	
	public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
				
		return repository.findById(formaPagamentoId).
				orElseThrow(() -> new FormaPagamentoNaoEncontradoException(formaPagamentoId));	
	}
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		
		return repository.save(formaPagamento);
	}
	
	@Transactional
	public void excluir(Long formaPagamentoId) {
		try {
			repository.deleteById(formaPagamentoId);
			repository.flush();
		}catch (EmptyResultDataAccessException e) {
			
			throw new FormaPagamentoNaoEncontradoException(formaPagamentoId);
			
		}catch (DataIntegrityViolationException e) {
			
			throw new EntidadeEmUsoException(String.format(MSG_ENTIDADE_EM_USO, formaPagamentoId));
		}
		
	}
}
