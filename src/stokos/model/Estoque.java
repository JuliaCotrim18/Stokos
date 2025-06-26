package stokos.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import stokos.exception.*;


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

    public void removerLotesVazios() 
    {
        // Remove da listaDeLotes cada 'lote' para o qual a condição 'lote.getQuantidade() == 0' for verdadeira.
        listaDeLotes.removeIf(lote -> lote.getQuantidade() == 0);
    }

    public int getQuantidadeDisponivel(String codigoDeBarra)
    {
        int quantidadeTotal = 0;
        for (Lote lote : this.listaDeLotes)
        {
            if (lote.getProduto().getCodigoDeBarras().equals(codigoDeBarra))
            {
                quantidadeTotal += lote.getQuantidade();
            }
        }

        return quantidadeTotal;

    }

    public void registrarVenda(String codigoDeBarras, int quantidadeParaRemover) throws ProdutoNaoCadastradoException, QuantidadeInsuficienteException
    {
        Produto produto = catalogo.buscarProduto(codigoDeBarras);

        if (produto == null)
        {
            throw new ProdutoNaoCadastradoException("Produto não cadastrado");
        }
        List<Lote> lotesDoProduto = new ArrayList<>();
        for (Lote lote : this.listaDeLotes) {
            if (lote.getProduto().getCodigoDeBarras().equals(codigoDeBarras)) {
                lotesDoProduto.add(lote);
            }
        }

        // 2. Ordenar a lista filtrada
        if (!lotesDoProduto.isEmpty()) {
            // Verifica se o produto é perecível
            boolean ehPerecivel = lotesDoProduto.get(0) instanceof LotePerecivel;

            if (ehPerecivel) {
                // Lógica FEFO: Ordena por data de validade
                Collections.sort(lotesDoProduto, new Comparator<Lote>() {
                    @Override
                    public int compare(Lote lote1, Lote lote2) {
                        LocalDate data1 = ((LotePerecivel) lote1).getDataDeValidade();
                        LocalDate data2 = ((LotePerecivel) lote2).getDataDeValidade();
                        return data1.compareTo(data2);
                    }
                });
            } else {
                // Lógica FIFO: Ordena por ID
                Collections.sort(lotesDoProduto, new Comparator<Lote>() {
                    @Override
                    public int compare(Lote lote1, Lote lote2) {
                        return Integer.compare(lote1.getId(), lote2.getId());
                    }
                });
            }
        }

        // 3. O resto da lógica para dar baixa nos lotes permanece igual
        int quantidadeRestante = quantidadeParaRemover;
        for (Lote lote : lotesDoProduto) {
            if (quantidadeRestante <= 0) break;

            int qtdNoLote = lote.getQuantidade();
            if (qtdNoLote >= quantidadeRestante) {
                lote.removeQuantidade(quantidadeRestante);
                quantidadeRestante = 0;
            } else {
                lote.removeQuantidade(qtdNoLote);
                quantidadeRestante -= qtdNoLote;
            }
        }

        // Atualiza o total vendido do produto.
        produto.registrarVenda(quantidadeParaRemover);
        
        // Limpa os lotes que ficaram com quantidade 0
        removerLotesVazios();



        


    }

    

}