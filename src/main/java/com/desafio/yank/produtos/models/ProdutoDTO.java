package com.desafio.yank.produtos.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoDTO {
    private String nome;
    private BigDecimal preco;
    private String url;
    private Integer classificacao;
    private BigDecimal desconto;

    public ProdutoDTO(String nome, BigDecimal preco, String url, Integer classificacao, BigDecimal desconto) {
        this.nome = nome;
        this.preco = preco;
        this.url = url;
        this.classificacao = classificacao;
        this.desconto = desconto;
    }
}
