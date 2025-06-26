package stokos.model;

// Lote.java


public class LoteNaoPerecivel extends Lote 
{

    public LoteNaoPerecivel(Produto produto, int quantidade)
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