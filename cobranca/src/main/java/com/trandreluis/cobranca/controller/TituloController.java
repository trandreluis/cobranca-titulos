package com.trandreluis.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trandreluis.cobranca.enums.StatusTitulo;
import com.trandreluis.cobranca.model.Titulo;
import com.trandreluis.cobranca.repository.TituloFilter;
import com.trandreluis.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	private CadastroTituloService titulosService;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mav = new ModelAndView(CADASTRO_VIEW);
		mav.addObject(new Titulo());
		return mav;
	}

	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
		List<Titulo> todosTitulos = titulosService.buscarTodos(filtro.getDescricao());
		ModelAndView mav = new ModelAndView("PesquisaTitulos");
		mav.addObject("todosTitulos", todosTitulos);

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}

		try {
			titulosService.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
			return "redirect:/titulos/novo";
		} catch (IllegalArgumentException error) {
			errors.rejectValue("dataVencimento", null, error.getMessage());
			return CADASTRO_VIEW;
		}
	}

	@RequestMapping("{id}")
	public ModelAndView editar(@PathVariable("id") Titulo titulo) {
		ModelAndView mav = new ModelAndView(CADASTRO_VIEW);
		mav.addObject(titulo);
		return mav;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		titulosService.excluir(id);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}

	@RequestMapping(value = "/{id}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long id) {
		return titulosService.receber(id);
	}
	
	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}