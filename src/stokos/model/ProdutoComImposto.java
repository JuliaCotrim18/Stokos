package stokos.model;

public class ProdutoComImposto extends Produto
{
    private static final long serialVersionUID = 1L;

    private double percentualIcms;

    public ProdutoComImposto(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza, double percentualIcsm)
    {
        super(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);

        this.percentualIcms = percentualIcsm;
    }

    public double getPercentualIcms()
    {
        return percentualIcms;
    }

    public void setPercentualIcms(double novoPercentual)
    {
        this.percentualIcms = novoPercentual;
    }

    @Override
    public double calcularLucro(double quantidade, double precoUnitario, double custoTotalDaVenda)
    {
        double receitaTotal = quantidade * precoUnitario;

        double valorDoImposto = receitaTotal * this.percentualIcms;

        return receitaTotal - custoTotalDaVenda - valorDoImposto;
    }
}