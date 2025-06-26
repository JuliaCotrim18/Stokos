// ProdutoComum.java

package stokos.model;

public class ProdutoComum extends Produto
{
    private static final long serialVersionUID = 1L;

    public ProdutoComum(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza)
    {
        super(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);
        
    }

    @Override
    public double calcularLucro(double quantidade, double precoUnitario, double custoTotalDaVenda)
    {
        double receitaTotal = quantidade * precoUnitario;

        return receitaTotal - custoTotalDaVenda;
    }
}