import java.time.LocalDate;

public class ProdutoPerecivel extends Produto 
{
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(int id, double precoUnitario, Grandeza grandeza, LocalDate dataDeValidade)
    {
        super(id, precoUnitario, grandeza);
        this.dataDeValidade = dataDeValidade;
    }

    public LocalDate getDataDeValidade()
    {
        return this.dataDeValidade;
    }

    public int getDiasParaVencimento()
    {
        return LocalDate.now().until(this.dataDeValidade).getDays();
    }

}
