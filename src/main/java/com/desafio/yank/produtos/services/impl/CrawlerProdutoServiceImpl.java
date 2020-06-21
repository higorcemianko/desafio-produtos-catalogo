package com.desafio.yank.produtos.services.impl;

import com.desafio.yank.produtos.models.Produto;
import com.desafio.yank.produtos.models.ProdutoDTO;
import com.desafio.yank.produtos.services.CrawlerProdutoService;

import com.desafio.yank.produtos.services.ProdutoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CrawlerProdutoServiceImpl implements CrawlerProdutoService {

    @Autowired
    private ProdutoService produtoService;

    private static final String CARD_PRODUTO_HOME = "a[class^=\"product-card__Wrapper-sc\"]";
    private static final String INFOS_PRODUTO_HOME = "div[class^=\"product-card__WrapperInfo\"]";
    private static final String STAMP_PRODUTO_HOME = "div[class^=\"product-card__Stamp-sc\"]";

    private static final String SUB_LINKS_HOME = "a[href]";
    private static final String CARD_PRODUTO_SUB_LINK = "div[class^=\"product-v2__ProductCardV2-sc\"]";
    private static final String STAMP_PRODUTO_SUB_LINK = "div[class^=\"StampUI-bwhjk3-9\"]";
    private static final String RIPPLE_CONTAINER_SUB_LINK = "div[class^=\"RippleContainer-sc\"]";
    private static final String LINK_PRODUTO_GRADE = "a[class^=\"Link-bwhjk3\"]";


    @Override
    public List<Produto> buscarProdutos(String url) {
        List<Produto> produtos = new ArrayList<>();
        try{
            Document document = Jsoup.connect(url).get();
            produtosHome(produtos, document);
            Elements subLinksHome = document.select(SUB_LINKS_HOME);
            for (Element el : subLinksHome){
                produtosSubLinksGrade(produtos, Jsoup.connect(el.attr("abs:href")).get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        produtos.forEach(produto -> produtoService.salvar(produto));
        return produtos;
    }

    @Override
    public Map<Integer, ProdutoDTO> filtrarProdutos(List<Produto> produtos) {
        Map<Integer, ProdutoDTO> retorno = new HashMap<>();
        int iMaisBarato = 0, iMaisPopular = 0, iMaiorDesconto = 0;
        for (int i = 1; i < produtos.size(); i++ ){
            if (produtos.get(i).getPreco().compareTo(produtos.get(iMaisBarato).getPreco()) < 0){
                iMaisBarato = i;
            }
            if (produtos.get(i).getClassicacao().compareTo(produtos.get(iMaisPopular).getClassicacao()) > 0){
                iMaisPopular = i;
            }
            if (produtos.get(i).getDesconto().compareTo(produtos.get(iMaiorDesconto).getDesconto()) > 0){
                iMaiorDesconto = i;
            }
        }
        retorno.put(1, new ProdutoDTO(produtos.get(iMaisBarato).getNome(), produtos.get(iMaisBarato).getPreco(), produtos.get(iMaisBarato).getUrl(), produtos.get(iMaisBarato).getClassicacao(), produtos.get(iMaisBarato).getDesconto()));
        retorno.put(2, new ProdutoDTO(produtos.get(iMaisPopular).getNome(), produtos.get(iMaisPopular).getPreco(), produtos.get(iMaisPopular).getUrl(), produtos.get(iMaisPopular).getClassicacao(), produtos.get(iMaisPopular).getDesconto()));
        retorno.put(3, new ProdutoDTO(produtos.get(iMaiorDesconto).getNome(), produtos.get(iMaiorDesconto).getPreco(), produtos.get(iMaiorDesconto).getUrl(), produtos.get(iMaiorDesconto).getClassicacao(), produtos.get(iMaiorDesconto).getDesconto()));
        return retorno;
    }


    private void produtosSubLinksGrade(List<Produto> produtos, Document document){
        Elements elementsSubLinksGrade = document.select(CARD_PRODUTO_SUB_LINK);
        for (Element el : elementsSubLinksGrade) {
            Elements stampProdutoGrade = el.select(STAMP_PRODUTO_SUB_LINK);
            Elements linkProdutoGrade = el.select(LINK_PRODUTO_GRADE);
            Elements nome = linkProdutoGrade.select("h2[class^=\"TitleUi-bwhjk3\"]");
            Elements classificacao = linkProdutoGrade.select("span[class^=\"Quantity-sc\"]");
            Elements preco = linkProdutoGrade.select("span[class^=\"PriceUI-bwhjk3\"]");
            String urlProduto = linkProdutoGrade.attr("abs:href");
            Elements desconto = stampProdutoGrade.select("span[class^=\"TextUI\"]");
            Produto produto = getProdutoSubLinkGrade(nome, classificacao, preco, urlProduto, desconto);
            if (Objects.nonNull(produto) && !produtos.contains(produto)) {
                produtos.add(produto);
            }
        }

    }

    private void produtosHome(List<Produto> produtos, Document document) {
        Elements elementsHome = document.select(CARD_PRODUTO_HOME);
        for (Element el : elementsHome){
            Elements infosProdutoHome = el.select(INFOS_PRODUTO_HOME);
            Elements stampProdutoHome = el.select(STAMP_PRODUTO_HOME);
            Elements nome = infosProdutoHome.select("span[class^=\"product-card__ProductName\"]");
            Elements descClassificacao = infosProdutoHome.select("desc[id^=\"svg-desc\"]");
            Elements preco = infosProdutoHome.select("span[class^=\"product-card__Price\"]");
            String urlProduto = el.attr("abs:href");
            Elements desconto = stampProdutoHome.select("span[class^=\"TextUI\"]");
            produtos.add(getProdutoHome(nome, descClassificacao, preco, urlProduto, desconto));
        }
    }

    private Produto getProdutoSubLinkGrade(Elements eNome, Elements eClassificacao, Elements ePreco, String urlProduto, Elements eDesconto){
        try{

            String nome = getHtmlElement(eNome);
            String sClassificação = getHtmlElement(eClassificacao);
            Integer classificacao = !StringUtils.isEmpty(sClassificação) ? new Integer(sClassificação.replaceAll("<!-- -->", "").replace("(", "").replace(")", "").trim()) : new Integer(0);
            String sPreco = getHtmlElement(ePreco);
            BigDecimal preco = !StringUtils.isEmpty(sPreco) ? new BigDecimal(sPreco.split("\n")[1].replaceAll("<!-- -->", "").replace(".", "").replace(",", ".").trim()) : BigDecimal.ZERO;
            String sDesconto = getHtmlElement(eDesconto);
            BigDecimal desconto = !StringUtils.isEmpty(sDesconto) ? new BigDecimal(sDesconto.replace("%", "").trim()) : BigDecimal.ZERO;
            return new Produto(nome, urlProduto, "", BigDecimal.ZERO, "", desconto, classificacao, preco);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }



    }

    private Produto getProdutoHome(Elements eNome, Elements eDescClassificacao, Elements ePreco, String urlProduto, Elements eDesconto){
        String nome = getHtmlElement(eNome);
        String descricaoClasificao = getHtmlElement(eDescClassificacao);
        Integer classificacao = new Integer(0);
        try{
            classificacao = !StringUtils.isEmpty(descricaoClasificao) ? new Integer(descricaoClasificao.split(" ")[3]) : classificacao;
        } catch (Exception e){
            classificacao = new Integer(0);
        }
        String sPreco = getHtmlElement(ePreco);
        BigDecimal preco = !StringUtils.isEmpty(sPreco) ? new BigDecimal(sPreco.replaceAll("R\\$", "").replace(".", "").replace(",", ".").trim()) : BigDecimal.ZERO;
        String sDesconto = getHtmlElement(eDesconto);
        BigDecimal desconto = !StringUtils.isEmpty(sDesconto) ? new BigDecimal(sDesconto.split("\n")[0]) : BigDecimal.ZERO;
//        System.out.println("Produto: " + nome);
//        System.out.println("    url: " + nome);
//        System.out.println("    desconto: " + desconto);
//        System.out.println("    classificao: " + classificacao);
//        System.out.println("    preco " + preco);


        return new Produto(nome, urlProduto, "", BigDecimal.ZERO, "", desconto, classificacao, preco);

    }

    private String getHtmlElement(Elements elements){
        return Objects.nonNull(elements) && elements.size() > 0 ? elements.get(0).html() : "";
    }

}
