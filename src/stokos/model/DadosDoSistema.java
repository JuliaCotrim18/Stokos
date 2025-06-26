package stokos.model;

// DadosDoSistema.java
import java.io.Serializable;

// Uma classe simples só para agrupar nossos dados principais para salvar/carregar.
public class DadosDoSistema implements Serializable {
    private static final long serialVersionUID = 1L;

    public CatalogoDeProdutos catalogo;
    public Estoque estoque;
    public HistoricoDeVendas historicoDeVendas;

    public DadosDoSistema()
    {
        this.catalogo = new CatalogoDeProdutos();
        this.historicoDeVendas = new HistoricoDeVendas();
    }
}