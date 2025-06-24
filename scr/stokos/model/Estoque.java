import java.io.Serializable;
import java.util.ArrayList;


// Esta classe representa o estoque da loja
// também funciona como uma espécie de banco de dados
// de produtos, onde podemos cadastrar, remover e atualizar produtos
// Uma instância será criada no começo da aplicação
// e é armazenada em um arquivo .dat
// ela guarda uma lista de lotes

public class Estoque implements Serializable // Serializable porque vamos serializar ele
{
    private ArrayList<Lote> listaDeLotes; // é uma lista de lotes disponiveis no estoque
    private final CatalogoDeProdutos catalogo; //referencia o CatalogoDeProdutos (Dependency Injection)

    public Estoque(CatalogoDeProdutos catalogo) //construtor
    {
        this.listaDeLotes = new ArrayList<Lote>();
        this.catalogo = catalogo; // recebe o catálogo de produtos
    }

    // métodos
    public ArrayList<Lote> getLotes()
    {
        return listaDeLotes;
    }

    
    public void adicionarLote(Lote lote) throws ProdutoNaoCadastradoException // adiciona um lote ao estoque
    {
        // primeiro verifica se o produto do lote está registrado no catálogo de produtos
        if (catalogo.verificaProdutoCadastrado(lote.getProduto().getCodigoDeBarras())) 
        {
            // se o produto já está cadastrado, adiciona o lote ao estoque
            listaDeLotes.add(lote);
        } 
        else 
        {
            // se o produto não está cadastrado, lança uma exceção
            throw new ProdutoNaoCadastradoException("Produto não cadastrado no catálogo: " + lote.getProduto().getNomeDoProduto());
        }

    }

}