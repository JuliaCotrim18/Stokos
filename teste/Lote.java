// Lote.java

import java.io.Serializable;

public class Lote implements Serializable
{
    private final Produto produto;
    private int quantidade; 
    private final Fornecedor fornecedor;

    public Lote(Produto produto, int quantidade, Fornecedor fornecedor)
    {
        this.produto = produto;
        this.quantidade = quantidadeInicial;
        this.fornecedor = fornecedor;
    }

    public Lote(Produto produto, int quantidade)
    {
        this(produto, quantidade, null); // Chama o outro construtor com fornecedor nulo
    }

    public Produto getProduto() 
    {
        return produto;
    }

    public int getQuantidade() 
    {
        return quantidade;
    }

    public Fornecedor getFornecedor() 
    {
        return fornecedor;
    }

    public void removeQuantidade(int quantidade)
    {
        if (quantidade <= 0) 
        {
            throw new IllegalArgumentException("Quantidade a remover deve ser maior que zero.");
        }
        if (quantidade > this.quantidade) 
        {
            throw new IllegalArgumentException("Quantidade a remover excede a quantidade disponível no lote.");
        }
        this.quantidade -= quantidade;
    }



}