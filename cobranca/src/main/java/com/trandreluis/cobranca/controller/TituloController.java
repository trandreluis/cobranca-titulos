package com.trandreluis.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trandreluis.cobranca.enums.StatusTitulo;
import com.trandreluis.cobranca.model.Titulo;
import com.trandreluis.cobranca.repository.Titulos;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	private static final String CADASTRO_VIEW = "CadastroTitulo";
	
	@Autowired
	private Titulos titulos;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mav = new ModelAndView(CADASTRO_VIEW);
		mav.addObject(new Titulo());
		return mav;
	}

	@RequestMapping
	public ModelAndView pesquisar() {
		List<Titulo> todosTitulos = titulos.findAll();
		ModelAndView mav = new ModelAndView("PesquisaTitulos");
		mav.addObject("todosTitulos", todosTitulos);
	
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return CADASTRO_VIEW;
		}
		
		try {			
			titulos.save(titulo);
			attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
		}catch(DataIntegrityViolationException error) {
			errors.rejectValue("dataVencimento", null, "Formato de data inválido.");
			return CADASTRO_VIEW;
		}
		return "redirect:/titulos/novo";
		
	}
	
	@RequestMapping("{id}")
	public ModelAndView editar(@PathVariable("id") Titulo titulo) {
		ModelAndView mav = new ModelAndView(CADASTRO_VIEW);
		mav.addObject(titulo);
		return mav;
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		titulos.delete(id);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}