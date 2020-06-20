package com.desafio.yank.produtos.controllers;

import com.desafio.yank.produtos.services.CrawlerProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProdutoController {

    @Autowired
    private CrawlerProdutoService crawlerProdutoService;

    @GetMapping(value = "/")
    public ModelAndView index() {
        crawlerProdutoService.buscarProdutos("http://www.americanas.com.br/");
        return new ModelAndView("index");
    }


}
