package stokos.model;

import java.io.Serializable;

public abstract class Lote implements Serializable
{
    private final Produto produto;
    private int quantidade;
    private final int id;

    static int quantidadeDeLotes = 0;

    // construtor
    public Lote(Produto produto, int quantidade)
    {
        this.id = ++quantidadeDeLotes;

        this.produto = produto;
        this.quantidade = quantidade;

    }
   

    // getters
    public Produto getProduto()
    {
        return produto;
    }
    public int getQuantidade()
    {
        return quantidade;
    }

    public int getId()
    {
        return this.id;
    }


    public abstract boolean loteVencido();

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

    public abstract boolean estaPertoDeVencer();
    
}