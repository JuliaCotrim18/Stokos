package stokos.model;

import java.time.LocalDate;

public class LotePerecivel extends Lote 
{
    private final LocalDate dataDeValidade;

    public LotePerecivel(Produto produto, int quantidadeInicial, LocalDate dataDeValidade)
    {
        super(produto, quantidadeInicial);
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

    @Override
    public boolean loteVencido()
    {
        return LocalDate.now().isAfter(dataDeValidade);
    }

    

}