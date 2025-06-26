package stokos.model;

import java.io.ObjectInputFilter.Config;
import java.time.LocalDate;
//import stokos.Config;

public class LotePerecivel extends Lote 
{
    private static final long serialVersionUID = 1L;
    private final LocalDate dataDeValidade;

    public LotePerecivel(Produto produto, double quantidadeInicial, LocalDate dataDeValidade)
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

    @Override
    public boolean estaPertoDeVencer()
    {
        if (loteVencido())
        {
            return false; // já venceu
        }
        return diasAteVencer() <= stokos.Config.DIAS_PARA_ESTAR_PROXIMO_DO_VENCIMENTO;
    }

    

}