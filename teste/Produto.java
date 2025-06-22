import java.io.Serializable;

public class Produto implements Serializable
{
  // Atributos
  private int id; // código para ser utilizado no próprio sistema
  private String codigoDeBarras = ""; // código de barras do produto
  private String categoria = ""; // categoria do produto
  private String nomeDoProduto = ""; // nome do produto ex. "Leite Longa Vida Desnatado 1L"
  private int quantidadeVendida = 0; // número de unidades vendidas
  private double precoUnitario; // preço por unidade do produto
  private final Grandeza grandeza; // o produto é medido em que grandeza (peso, unidade, volume)
  private static int contadorProdutos = 0; // contador para gerar IDs únicos

  // Método construtor
  public Produto(String nomeDoProduto, double precoUnitario, Grandeza grandeza, String codigoDeBarras)
  {
    // Isso é o essencial de um produto genérico, ele tem que ter pelo menos isso aqui

    this.id = ++contadorProdutos; // incrementa o contador de produtos para gerar um ID único
  

    this.nomeDoProduto = nomeDoProduto;
    this.precoUnitario = precoUnitario;
    this.grandeza = grandeza;
    this.codigoDeBarras = codigoDeBarras;

    
  }

  // Quantidade de produtos cadastrados
  public static int getTotalProdutosCadastrados() 
  {
      return contadorProdutos;
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

  public void registraVenda(int quantidadeVendida)
  {
    if (quantidadeVendida <= 0) {
      throw new IllegalArgumentException("Quantidade vendida deve ser maior que zero.");
    }
    this.quantidadeVendida += quantidadeVendida; // incrementa a quantidade vendida
  }

  public void setPrecoUnitario(double precoUnitario)
  {
    if (precoUnitario < 0) {
      throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
    }
    this.precoUnitario = precoUnitario;
  }

  @Override
  public String toString() 
  {
      return "ID: " + id + " | Produto: " + nomeDoProduto + " | Qtd: " + quantidadeDisponivel;
  }
}

