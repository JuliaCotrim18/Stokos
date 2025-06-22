// DadosDoSistema.java
import java.io.Serializable;

// Uma classe simples só para agrupar nossos dados principais para salvar/carregar.
public class DadosDoSistema implements Serializable {
    public CatalogoDeProdutos catalogo;
    public Estoque estoque;
}