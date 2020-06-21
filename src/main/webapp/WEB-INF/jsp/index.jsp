<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
<title>Yank - Produtos</title>
</head>
<body>
    <div class="container mt-3">
        <div class="jumbotron jumbotron-fluid">
          <div class="container">
            <h1 class="display-4">Catalogo de Produtos - Lojas Americanas</h1>
            <p class="lead">Desafio E-Commerce - Yank Solutions</p>
          </div>
        </div>
    </div>
    <div class="container">
        <div class="card">
            <div class="card-header">
              Produto Mais Barato
            </div>
            <div class="card-body">
              <h5 class="card-title">${produtoMaisBarato.nome}</h5>
              <p class="card-text">Preço: R$ ${produtoMaisBarato.preco}</p>
              <p class="card-text">Avaliações: ${produtoMaisBarato.classificacao}</p>
              <p class="card-text">Desconto:  ${produtoMaisBarato.desconto} %</p>
              <a href="${produtoMaisBarato.url}" class="btn btn-primary">Visite no site</a>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="card">
            <div class="card-header">
              Produto Mais Popular
            </div>
            <div class="card-body">
              <h5 class="card-title">${produtoMaisPopular.nome}</h5>
              <p class="card-text">Preço: R$ ${produtoMaisPopular.preco}</p>
              <p class="card-text">Avaliações: ${produtoMaisPopular.classificacao}</p>
              <p class="card-text">Desconto:  ${produtoMaisPopular.desconto} %</p>
              <a href="${produtoMaisPopular.url}" class="btn btn-primary">Visite no site</a>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="card">
            <div class="card-header">
              Produto Com Maior Desconto
            </div>
            <div class="card-body">
              <h5 class="card-title">${produtoMaiorDesconto.nome}</h5>
              <p class="card-text">Preço: R$ ${produtoMaiorDesconto.preco}</p>
              <p class="card-text">Avaliações: ${produtoMaiorDesconto.classificacao}</p>
              <p class="card-text">Desconto:  ${produtoMaiorDesconto.desconto} %</p>
              <a href="${produtoMaiorDesconto.url}" class="btn btn-primary">Visite no site</a>
            </div>
        </div>
    </div>
</body>
</html>