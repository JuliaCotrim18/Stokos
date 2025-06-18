package modelo;

import java.io.Serializable;

public class Produto implements Serializable
{
  // Atributos
  
  private int id; // código para ser utilizado no próprio sistema
  private String codigoDeBarras = ""; // código de barras do produto
  private int quantidadeDisponivel = 0; // quantidade em estoque
  private String fornecedor = ""; // fornecedor do produto
  private String categoria = ""; // categoria do produto
  private String nomeDoProduto = ""; // nome do produto ex. sabão 
  private int quantidadeVendida = 0; // número de unidades vendidas
  private double precoUnitario; // preço por unidade do produto
  private Grandeza grandeza; // o produto é medido em que grandeza (peso, unidade, volume)

  // Método construtor
  public Produto(int id, double precoUnitario, Grandeza grandeza)
  {
    this.id = id;
    this.precoUnitario = precoUnitario;
    this.grandeza = grandeza;
    
  }

  // getters e setters
  public int getId()
  {
    return this.id;
  }

  public String getCodigoDeBarras()
  {
    return this.codigoDeBarras;
  }

  public String getFornecedor()
  {
    return this.fornecedor;
  }

  public String getCategoria()
  {
    return this.categoria;
  }

  public String getNomeDoProduto()
  {
    return this.nomeDoProduto;
  }

  public int getQuantidadeDisponivel()
  {
    return this.quantidadeDisponivel;
  }

  public int getQuantidadeVendida()
  {
    return this.quantidadeVendida;
  }

  public double getPrecoUnitario()
  {
    return this.precoUnitario;
  }

  public Grandeza getGrandeza()
  {
    return this.grandeza;
  }

  public void setCodigoDeBarras(String codigoDeBarras)
  {
    this.codigoDeBarras = codigoDeBarras;
  }

  public void setFornecedor(String fornecedor)
  {
    this.fornecedor = fornecedor;
  }

  public void setCategoria(String categoria)
  {
    this.categoria = categoria;
  }

  public void setNomeDoProduto(String nomeDoProduto)
  {
    this.nomeDoProduto = nomeDoProduto;
  }

  public void adicionaQuantidade(int quantidade)
  {
    if (quantidade < 0)
    {
      throw new IllegalArgumentException("Quantidade não pode ser negativa");
    }

    this.quantidadeDisponivel += quantidade;
  }

  public void removeQuantidade(int quantidade)
  {
    if (quantidade < 0)
    {
      throw new IllegalArgumentException("Quantidade não pode ser negativa");
    }

    if (quantidade > this.quantidadeDisponivel)
    {
      throw new IllegalArgumentException("Quantidade a remover excede a quantidade disponível");
    }

    this.quantidadeDisponivel -= quantidade;
  }

  public void vendeQuantidade(int quantidade)
  {
    if (quantidade < 0)
    {
      throw new IllegalArgumentException("Quantidade não pode ser negativa");
    }

    if (quantidade > this.quantidadeDisponivel)
    {
      throw new IllegalArgumentException("Quantidade a vender excede a quantidade disponível");
    }

    this.quantidadeDisponivel -= quantidade;
    this.quantidadeVendida += quantidade;
  }
}
