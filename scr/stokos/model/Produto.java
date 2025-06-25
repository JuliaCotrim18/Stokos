package stokos.model;

import java.io.Serializable;


// A classe Produto representa um produto genérico
// ela funciona como uma espécie de ficha cadastral do produto
// ela contém informações básicas como nome, preço, categoria, código de barras, etc.
public class Produto implements Serializable
{
  // Atributos
  private int id; // código para ser utilizado no próprio sistema, o ID é único para cada produto e é gerado automaticamente quando um novo produto é criado
  private String codigoDeBarras; // código de barras do produto
  private String categoria; // categoria do produto "Laticínios", "Hortifruti", "Padaria", etc.
  private String nomeDoProduto; // nome do produto ex. "Leite Longa Vida Desnatado 1L"
  private int quantidadeVendida; // número de unidades vendidas
  private double precoUnitario; // preço por unidade do produto
  private final Grandeza grandeza; // o produto é medido em que grandeza (PESO, UNIDADE ou VOLUME)

  // contadorProdutos é um contador estático que é utilizado para gerar IDs únicos para os produtos
  private static int contadorProdutos = 0; // contador para gerar IDs únicos

  // Método construtor
  public Produto(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza)
  {
    // Isso é o essencial de um produto genérico, ele tem que ter pelo menos isso aqui

    this.id = ++contadorProdutos; // incrementa o contador de produtos para gerar um ID único
  

    this.nomeDoProduto = nomeDoProduto;
    this.precoUnitario = precoUnitario;
    this.grandeza = grandeza;
    this.codigoDeBarras = codigoDeBarras;

    this.quantidadeVendida = 0; // inicializa a quantidade vendida como 0
    
    
  }

  // Quantidade de produtos cadastrados
  public static int getTotalProdutosCadastrados() 
  {
      return contadorProdutos;
  }


  // getters 
  public int getId()
  {
    return this.id;
  }

  public String getCodigoDeBarras()
  {
    return this.codigoDeBarras;
  }


  public String getCategoria()
  {
    return this.categoria;
  }

  public String getNomeDoProduto()
  {
    return this.nomeDoProduto;
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

  //setters
  public void setCodigoDeBarras(String codigoDeBarras) // talvez o código de barras mude, então vamos permitir que ele seja alterado
  {
    this.codigoDeBarras = codigoDeBarras;
  }


  public void setCategoria(String categoria) 
  {
    this.categoria = categoria;
  }

  public void setNomeDoProduto(String nomeDoProduto)
  {
    this.nomeDoProduto = nomeDoProduto;
  }

    public void setPrecoUnitario(double precoUnitario)
  {
    if (precoUnitario < 0) {
      throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
    }
    this.precoUnitario = precoUnitario;
  }

  // Outros métodos
  public void registraVenda(int quantidadeVendida)
  {
    if (quantidadeVendida <= 0) {
      throw new IllegalArgumentException("Quantidade vendida deve ser maior que zero.");
    }
    this.quantidadeVendida += quantidadeVendida; // incrementa a quantidade vendida
  }



}

