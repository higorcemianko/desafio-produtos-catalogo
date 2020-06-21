package com.desafio.yank.produtos.controllers;

import com.desafio.yank.produtos.models.Produto;
import com.desafio.yank.produtos.models.ProdutoDTO;
import com.desafio.yank.produtos.services.CrawlerProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ProdutoController {

    @Autowired
    private CrawlerProdutoService crawlerProdutoService;

    @GetMapping(value = "/")
    public ModelAndView index() {
        List<Produto> produtos = crawlerProdutoService.buscarProdutos("http://www.americanas.com.br/");
        Map<Integer, ProdutoDTO> filtrados = crawlerProdutoService.filtrarProdutos(produtos);
        return new ModelAndView("index")
                .addObject("produtoMaisBarato", filtrados.get(1))
                .addObject("produtoMaisPopular", filtrados.get(2))
                .addObject("produtoMaiorDesconto", filtrados.get(3));


    }


}
