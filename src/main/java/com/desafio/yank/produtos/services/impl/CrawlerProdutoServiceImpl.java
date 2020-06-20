package com.desafio.yank.produtos.services.impl;

import com.desafio.yank.produtos.models.Produto;
import com.desafio.yank.produtos.services.CrawlerProdutoService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CrawlerProdutoServiceImpl implements CrawlerProdutoService {

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
        produtos.forEach(System.out::println);
        return produtos;
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
