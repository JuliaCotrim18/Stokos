package stokos.model;

import java.io.Serializable;

public abstract class Lote implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final Produto produto;
    private double quantidade;
    private final int id;
    private String fornecedor;
    private double custoDoLote;
    private final double quantidadeInicial;

    static int quantidadeDeLotes = 0;

    // construtor
    public Lote(Produto produto, double quantidade)
    {
        this.id = ++quantidadeDeLotes;

        this.produto = produto;
        this.quantidade = quantidade;

        this.fornecedor = "Não informado";
        this.custoDoLote = 0.0;

        this.quantidadeInicial = quantidade;

    }
   

    // getters
    public Produto getProduto()
    {
        return produto;
    }
    public double getQuantidade()
    {
        return quantidade;
    }

    public int getId()
    {
        return this.id;
    }

    public String getFornecedor()
    {
        return fornecedor;
    }

    public double getCustoDoLote()
    {
        return custoDoLote;
    }

    public double getQuantidadeInicial()
    {
        return this.quantidadeInicial;
    }

    // setters
    public void setFornecedor(String fornecedor)
    {
        this.fornecedor = fornecedor;
    }

    public void setCustoDoLote(double custoDoLote)
    {
        this.custoDoLote = custoDoLote;
    }


    public abstract boolean loteVencido();

    public void removeQuantidade(double quantidade)
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

    public abstract boolean estaPertoDeVencer();
    
}