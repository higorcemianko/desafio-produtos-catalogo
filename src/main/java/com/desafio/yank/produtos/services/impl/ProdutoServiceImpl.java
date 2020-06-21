package com.desafio.yank.produtos.services.impl;

import com.desafio.yank.produtos.models.Produto;
import com.desafio.yank.produtos.repositories.ProdutoRepository;
import com.desafio.yank.produtos.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl implements ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public void salvar(Produto produto) {
        produtoRepository.save(produto);
    }
}
