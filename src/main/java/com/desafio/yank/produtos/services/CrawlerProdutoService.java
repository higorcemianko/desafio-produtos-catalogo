package com.desafio.yank.produtos.services;

import com.desafio.yank.produtos.models.Produto;

import java.util.List;

public interface CrawlerProdutoService {
    List<Produto> buscarProdutos(String url);
}
