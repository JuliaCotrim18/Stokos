package stokos.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import stokos.model.RegistroDeVenda;
import stokos.exception.*;

/**
 * A classe Estoque é uma das classes centrais do domínio da aplicação.
 * Ela gerencia o conjunto de todos os lotes de produtos disponíveis na loja.
 * Funciona como uma representação do armazenamento físico, controlando as
 * entradas (adição de lotes) e saídas (registro de vendas) de produtos.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Encapsulamento: Os detalhes de implementação da lista de lotes (`listaDeLotes`)
 * são privados e o acesso é controlado através de métodos públicos (`adicionarLote`,
 * `registrarVenda`, etc.), garantindo a integridade dos dados.
 * - Injeção de Dependência: A classe não cria sua própria instância de
 * `CatalogoDeProdutos`. Em vez disso, ela a recebe em seu construtor. Isso
 * desacopla o Estoque do Catálogo, facilitando a manutenção e os testes,
 * pois permite "injetar" um catálogo falso (mock) se necessário.
 * - Serializable: A classe implementa a interface `Serializable`, o que permite
 * que seus objetos (e todos os objetos que ela contém, como a `listaDeLotes`)
 * sejam convertidos em uma sequência de bytes. Isso é fundamental para o
 * serviço de persistência, que salva o estado do sistema em um arquivo.
 */
public class Estoque implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributo para armazenar a coleção de lotes.
    // O uso de ArrayList permite uma coleção dinâmica que pode crescer ou encolher.
    private ArrayList<Lote> listaDeLotes;

    // Atributo final para a referência ao catálogo de produtos.
    // 'final' indica que, uma vez que a referência é atribuída no construtor,
    // ela não pode mais ser alterada. Isso garante que o estoque sempre se
    // refira ao mesmo catálogo durante todo o seu ciclo de vida.
    private final CatalogoDeProdutos catalogo;

    /**
     * Construtor da classe Estoque.
     * @param catalogo Uma instância de CatalogoDeProdutos que será usada para
     * validar a existência dos produtos antes de adicionar um lote.
     * Este é um exemplo claro de Injeção de Dependência.
     */
    public Estoque(CatalogoDeProdutos catalogo) {
        this.listaDeLotes = new ArrayList<>();
        this.catalogo = catalogo;
    }

    /**
     * Retorna a lista completa de lotes atualmente no estoque.
     * @return um `ArrayList<Lote>` contendo todos os lotes.
     */
    public ArrayList<Lote> getLotes() {
        return listaDeLotes;
    }

    /**
     * Adiciona um novo lote ao estoque.
     * Antes de adicionar, verifica se o produto associado ao lote já está
     * devidamente cadastrado no catálogo de produtos.
     *
     * @param lote O objeto Lote a ser adicionado.
     * **POLIMORFISMO DE TIPO:** Este método aceita qualquer objeto que seja um `Lote`
     * ou uma de suas subclasses (como `LotePerecivel` ou `LoteNaoPerecivel`).
     * O método não precisa saber qual é o tipo específico de lote,
     * demonstrando flexibilidade e extensibilidade.
     * @throws ProdutoNaoCadastradoException se o produto do lote não for encontrado no catálogo.
     */
    public void adicionarLote(Lote lote) throws ProdutoNaoCadastradoException {
        // Validação crucial para manter a consistência do sistema.
        // Um lote só pode ser adicionado se seu respectivo produto existir.
        if (catalogo.verificaProdutoCadastrado(lote.getProduto().getCodigoDeBarras())) {
            listaDeLotes.add(lote);
        } else {
            // Lançar uma exceção específica torna o tratamento de erros mais claro
            // para quem chama o método.
            throw new ProdutoNaoCadastradoException("Produto não cadastrado no catálogo: " + lote.getProduto().getNomeDoProduto());
        }
    }

    /**
     * Remove todos os lotes que não possuem mais quantidade (quantidade == 0).
     * Este método é útil para limpeza, otimizando o desempenho de outras operações
     * ao reduzir o número de itens a serem percorridos na lista.
     * Utiliza um método funcional (lambda) `removeIf`, que é mais conciso e legível.
     */
    public void removerLotesVazios() {
        listaDeLotes.removeIf(lote -> lote.getQuantidade() == 0);
    }

    /**
     * Calcula e retorna a quantidade total disponível de um produto específico,
     * somando as quantidades de todos os seus lotes no estoque.
     *
     * @param codigoDeBarra O código de barras do produto a ser consultado.
     * @return A quantidade total disponível do produto (double).
     */
    public double getQuantidadeDisponivel(String codigoDeBarra) {
        double quantidadeTotal = 0;
        for (Lote lote : this.listaDeLotes) {
            // Percorre todos os lotes e acumula a quantidade se o código de barras corresponder.
            if (lote.getProduto().getCodigoDeBarras().equals(codigoDeBarra)) {
                quantidadeTotal += lote.getQuantidade();
            }
        }
        return quantidadeTotal;
    }

    /**
     * Processa a baixa de uma determinada quantidade de um produto do estoque.
     * Este é um dos métodos mais complexos e importantes, pois implementa a
     * lógica de negócio de FIFO (First-In, First-Out) e FEFO (First-Expire, First-Out).
     *
     * @param codigoDeBarras O código do produto vendido.
     * @param quantidadeParaRemover A quantidade a ser retirada do estoque.
     * @param historico A referência ao histórico de vendas para registrar a transação.
     * @throws ProdutoNaoCadastradoException Se o produto não for encontrado.
     * @throws QuantidadeInsuficienteException Se a quantidade em estoque for menor que a solicitada.
     */
    public void registrarVenda(String codigoDeBarras, double quantidadeParaRemover, HistoricoDeVendas historico)
            throws ProdutoNaoCadastradoException, QuantidadeInsuficienteException {

        // 1. Validar e buscar o produto no catálogo.
        Produto produto = catalogo.buscarProduto(codigoDeBarras);
        if (produto == null) {
            throw new ProdutoNaoCadastradoException("Produto não cadastrado");
        }
        
        // Validação de estoque antes de iniciar o processo de baixa
        if (getQuantidadeDisponivel(codigoDeBarras) < quantidadeParaRemover) {
            throw new QuantidadeInsuficienteException("Quantidade em estoque insuficiente para o produto: " + produto.getNomeDoProduto());
        }

        // 2. Filtrar todos os lotes correspondentes ao produto vendido.
        List<Lote> lotesDoProduto = new ArrayList<>();
        for (Lote lote : this.listaDeLotes) {
            if (lote.getProduto().getCodigoDeBarras().equals(codigoDeBarras)) {
                lotesDoProduto.add(lote);
            }
        }

        // 3. Ordenar a lista de lotes de acordo com a estratégia (FIFO ou FEFO).
        // Este bloco é um exemplo excelente de polimorfismo e verificação de tipo (instanceof).
        if (!lotesDoProduto.isEmpty()) {
            // Verifica o tipo do primeiro lote para decidir a estratégia de ordenação.
            // O operador 'instanceof' permite verificar se um objeto é de uma subclasse específica.
            boolean ehPerecivel = lotesDoProduto.get(0) instanceof LotePerecivel;

            if (ehPerecivel) {
                // ESTRATÉGIA FEFO (First-Expire, First-Out): para produtos perecíveis.
                // Ordena os lotes pela data de validade, do mais próximo ao mais distante.
                // O casting `(LotePerecivel)` é necessário para acessar o método `getDataDeValidade()`,
                // que só existe na subclasse `LotePerecivel`.
                Collections.sort(lotesDoProduto, Comparator.comparing(lote -> ((LotePerecivel) lote).getDataDeValidade()));
            } else {
                // ESTRATÉGIA FIFO (First-In, First-Out): para produtos não-perecíveis.
                // Ordena os lotes pelo ID, garantindo que o lote mais antigo (menor ID) seja consumido primeiro.
                Collections.sort(lotesDoProduto, Comparator.comparingInt(Lote::getId));
            }
        }

        // 4. Dar baixa da quantidade nos lotes ordenados e calcular o custo.
        double custoTotalDaVenda = 0.0;
        double quantidadeRestante = quantidadeParaRemover;
        for (Lote lote : lotesDoProduto) {
            if (quantidadeRestante <= 0) break; // Encerra se a quantidade já foi totalmente removida.

            double qtdNoLote = lote.getQuantidade();
            double custoUnitarioDoLote = lote.getCustoDoLote() / lote.getQuantidadeInicial();

            if (qtdNoLote >= quantidadeRestante) {
                // O lote atual tem quantidade suficiente para suprir o restante da venda.
                custoTotalDaVenda += quantidadeRestante * custoUnitarioDoLote;
                lote.removeQuantidade(quantidadeRestante);
                quantidadeRestante = 0;
            } else {
                // O lote atual será totalmente consumido.
                custoTotalDaVenda += qtdNoLote * custoUnitarioDoLote;
                lote.removeQuantidade(qtdNoLote);
                quantidadeRestante -= qtdNoLote; // Atualiza a quantidade que ainda precisa ser removida.
            }
        }

        // 5. Atualizar registros e limpar o sistema.
        produto.registrarVenda(quantidadeParaRemover);
        removerLotesVazios();

        // 6. Criar e adicionar o registro da venda ao histórico.
        // A criação do RegistroDeVenda também demonstra polimorfismo, pois ele
        // delega o cálculo do lucro para o objeto 'produto', que pode ser
        // de diferentes tipos (Comum ou ComImposto).
        RegistroDeVenda registro = new RegistroDeVenda(produto, quantidadeParaRemover, custoTotalDaVenda);
        historico.adicionarRegistro(registro);
    }
}