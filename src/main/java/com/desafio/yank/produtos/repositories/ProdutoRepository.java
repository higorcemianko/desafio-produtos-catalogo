package com.desafio.yank.produtos.repositories;

import com.desafio.yank.produtos.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
