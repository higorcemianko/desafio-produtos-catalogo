package com.desafio.yank.produtos.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "PRODUTO")
@Getter
@Setter
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "url")
    private String url;


    @Column(name = "categoria")
    private String categoria;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "cor")
    private String cor;

    @Column(name = "desconto")
    private BigDecimal desconto;

    @Column(name = "classificacao")
    private Integer classicacao;

    @Column(name = "preco")
    private BigDecimal preco;

    public Produto(String nome, String url, String categoria, BigDecimal peso, String cor, BigDecimal desconto, Integer classicacao, BigDecimal preco) {
        this.nome = nome;
        this.url = url;
        this.categoria = categoria;
        this.peso = peso;
        this.cor = cor;
        this.desconto = desconto;
        this.classicacao = classicacao;
        this.preco = preco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(nome, produto.nome);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", url='" + url + '\'' +
                ", categoria='" + categoria + '\'' +
                ", peso=" + peso +
                ", cor='" + cor + '\'' +
                ", desconto=" + desconto +
                ", classicacao=" + classicacao +
                ", preco=" + preco +
                '}';
    }
}
