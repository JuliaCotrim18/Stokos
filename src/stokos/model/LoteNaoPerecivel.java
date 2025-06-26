package stokos.model;

// Lote.java


public class LoteNaoPerecivel extends Lote 
{
    private static final long serialVersionUID = 1L;

    public LoteNaoPerecivel(Produto produto, double quantidade)
    {
        super(produto, quantidade);
    }

   
    @Override
    public boolean loteVencido()
    {
        // Lotes não perecíveis não vencem, então sempre retorna false
        return false;
    }

    @Override
    public boolean estaPertoDeVencer()
    {
        return false; // lotes não-pereciveis nunca vão vencer
    }



}