package stokos.model;

public class ProdutoComICMS extends Produto
{
    private double icms;

    public ProdutoComICMS(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza, double icms)
    {
        super();
        this.icms = icms;
    }

    public double getIcms()
    {
        return this.icms;
    }

    public void setIcsm(double novoIcms)
    {
        this.icms = novoIcms;
    }
}