package com.trandreluis.cobranca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.trandreluis.cobranca.enums.StatusTitulo;
import com.trandreluis.cobranca.model.Titulo;
import com.trandreluis.cobranca.repository.Titulos;

@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;
	
	public void salvar(Titulo titulo) {
		try {
			titulos.save(titulo);	
		}catch(DataIntegrityViolationException error) {
			throw new IllegalArgumentException("Formato de data inválido!");
		}
	}

	public String receber(Long id) {
		Titulo titulo = titulos.findOne(id);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(titulo);
		return titulo.getStatus().getDescricao();
	}
	
	public void excluir(Long id) {
		titulos.delete(id);
	}

	public List<Titulo> buscarTodos(String descricao) {
		descricao = descricao == null ? "" : descricao;
		return titulos.findByDescricaoContaining(descricao);
	}
	
}