import java.io.Serializable;

public abstract class Produto implements Serializable
{
  // Atributos
  
  private int id; // código para ser utilizado no próprio sistema
  private String codigoDeBarras = ""; // código de barras do produto
  private int quantidadeDisponivel = 0; // quantidade em estoque
  private String categoria = ""; // categoria do produto
  private String nomeDoProduto = ""; // nome do produto ex. sabão 
  private int quantidadeVendida = 0; // número de unidades vendidas
  private double precoUnitario; // preço por unidade do produto
  private Grandeza grandeza; // o produto é medido em que grandeza (peso, unidade, volume)
  private static int contadorProdutos = 0; // contador para gerar IDs únicos
  private Fornecedor fornecedor; // fornecedor do produto

  // Método construtor
  public Produto(String nomeDoProduto, double precoUnitario, Grandeza grandeza, Fornecedor fornecedor)
  {
    this.id = ++contadorProdutos; // ID é gerado automaticamente
    this.nomeDoProduto = nomeDoProduto;
    this.precoUnitario = precoUnitario;
    this.grandeza = grandeza;
    this.fornecedor = fornecedor;
    
  }

  // Método construtor: caso o fornecedor não seja necessário, podemos ter um construtor que não o recebe
  public Produto(String nomeDoProduto, double precoUnitario, Grandeza grandeza) 
  {
    this(nomeDoProduto, precoUnitario, grandeza, null); 
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

  public Fornecedor getFornecedor() 
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

  public void setFornecedor(Fornecedor fornecedor)
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

  public abstract String getDetalhes(); // Método abstrato que deve ser implementado pelas subclasses para fornecer detalhes específicos do produto

  @Override
  public String toString() 
  {
      return "ID: " + id + " | Produto: " + nomeDoProduto + " | Qtd: " + quantidadeDisponivel;
  }
}
// 
