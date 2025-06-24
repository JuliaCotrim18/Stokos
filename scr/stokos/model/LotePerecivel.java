import java.time.LocalDate;

public class LotePerecivel extends Lote
{
    private final LocalDate dataDeValidade;

    LotePerecivel(Produto produto, int quantidadeInicial, Fornecedor fornecedor, LocalDate dataDeValidade)
    {
        super(produto, quantidadeInicial, fornecedor);
        this.dataDeValidade = dataDeValidade;
    }

    public LocalDate getDataDeValidade()
    {
        return dataDeValidade;
    }

    public int diasAteVencer()
    {
        return LocalDate.now().until(dataDeValidade).getDays();
    }

    

}