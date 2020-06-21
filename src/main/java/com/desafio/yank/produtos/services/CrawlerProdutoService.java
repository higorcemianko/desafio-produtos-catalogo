package com.desafio.yank.produtos.services;

import com.desafio.yank.produtos.models.Produto;
import com.desafio.yank.produtos.models.ProdutoDTO;

import java.util.List;
import java.util.Map;

public interface CrawlerProdutoService {
    List<Produto> buscarProdutos(String url);

    Map<Integer, ProdutoDTO> filtrarProdutos(List<Produto> produtos);
    
}
