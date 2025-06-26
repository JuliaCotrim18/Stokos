package stokos.model;

// CatalogoDeProdutos.java

import stokos.exception.ProdutoNaoCadastradoException;
import stokos.exception.ProdutoJaCadastradoException; 

import java.io.Serializable;
import java.util.ArrayList;



public class CatalogoDeProdutos implements Serializable
{
    private ArrayList<Produto> listaDeProdutos;

    public CatalogoDeProdutos() 
    {
        this.listaDeProdutos = new ArrayList<>();
    }

    public boolean verificaProdutoCadastrado(String codigoDeBarras)
    {
        for (Produto produto : listaDeProdutos) {
            if (produto.getCodigoDeBarras().equals(codigoDeBarras)) {
                return true; // Produto encontrado
            }
        }
        return false; // Produto não encontrado
    }

    public void cadastrarProduto(Produto produto) throws ProdutoJaCadastradoException 
    {
        // verifica se o produto já está cadastrado
        if ( verificaProdutoCadastrado(produto.getCodigoDeBarras()) )
        {
            throw new ProdutoJaCadastradoException("Produto já cadastrado: " + produto.getCodigoDeBarras());
        }

        listaDeProdutos.add(produto);
    }
    
    public void removerProduto(String codigoDeBarras) throws ProdutoNaoCadastradoException 
    {
        Produto produtoParaRemover = null;

        for (Produto p : listaDeProdutos) 
        {
            if (p.getCodigoDeBarras().equals(codigoDeBarras)) 
            {
                produtoParaRemover = p;
                break;
            }
        }

        if (produtoParaRemover == null) {
            throw new ProdutoNaoCadastradoException("Produto não encontrado: " + codigoDeBarras);
        }

        listaDeProdutos.remove(produtoParaRemover);
    }



    /**
     * Busca um produto no catálogo pelo seu código de barras.
     * @param codigoDeBarras O código de barras a ser procurado.
     * @return O objeto Produto se for encontrado, ou null se não for.
     */
    public Produto buscarProduto(String codigoDeBarras) 
    {
        for (Produto produto : listaDeProdutos) {
            if (produto.getCodigoDeBarras().equals(codigoDeBarras)) {
                return produto; // Encontrou! Retorna o objeto produto.
            }
        }
        return null; // Não encontrou, retorna null.
    }

    public ArrayList<Produto> getListaDeProduto()
    {
        return this.listaDeProdutos;
    }
}