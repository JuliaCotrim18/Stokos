package stokos.model;

import java.io.Serializable;

/**
 * A classe Lote é uma classe ABSTRATA que serve como modelo base para
 * todos os tipos de lotes no sistema. Ela representa uma remessa específica
 * de um produto, com uma quantidade, fornecedor e custo definidos.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Abstração: A classe é declarada como 'abstract', significando que ela não
 * pode ser instanciada diretamente. Ela foi projetada para ser uma superclasse,
 * fornecendo atributos e comportamentos comuns que serão herdados por subclasses
 * concretas, como `LotePerecivel` e `LoteNaoPerecivel`.
 * - Herança: As subclasses herdarão todos os campos e métodos desta classe,
 * promovendo a reutilização de código.
 * - Encapsulamento: Os atributos são privados, e o acesso é controlado
 * por meio de métodos getters e setters, protegendo o estado interno do objeto.
 * - Métodos Abstratos: A classe define métodos abstratos como `loteVencido()`
 * e `estaPertoDeVencer()`. Isso estabelece um "contrato" que obriga qualquer
 * subclasse a fornecer sua própria implementação para esses métodos, garantindo
 * um comportamento polimórfico.
 * - Serializable: Implementa a interface para que os lotes possam ser salvos
 * juntamente com o resto dos dados do sistema.
 */
public abstract class Lote implements Serializable {

    // Identificador de versão para a serialização.
    private static final long serialVersionUID = 1L;

    // Atributo 'final' porque um lote sempre se refere ao mesmo produto.
    private final Produto produto;
    // A quantidade atual do lote, que diminui com as vendas.
    private double quantidade;
    // ID único para cada lote, 'final' para garantir que não mude após a criação.
    private final int id;
    // Informações adicionais do lote.
    private String fornecedor;
    private double custoDoLote;
    // 'final' pois a quantidade inicial é usada como base para cálculos de custo.
    private final double quantidadeInicial;

    // Atributo 'static' para gerar IDs únicos para cada lote criado.
    // Sendo 'static', este contador é compartilhado por todas as instâncias da classe Lote.
    static int quantidadeDeLotes = 0;

    /**
     * Construtor da classe abstrata Lote.
     * É chamado pelas subclasses para inicializar os atributos comuns.
     *
     * @param produto A referência para o objeto Produto ao qual este lote pertence.
     * @param quantidade A quantidade inicial de itens neste lote.
     */
    public Lote(Produto produto, double quantidade) {
        // Incrementa o contador estático e atribui o novo valor como ID do lote.
        // Isso garante que cada lote terá um ID sequencial e único.
        this.id = ++quantidadeDeLotes;

        this.produto = produto;
        this.quantidade = quantidade;
        this.quantidadeInicial = quantidade; // Guarda a quantidade original.

        // Define valores padrão para os atributos opcionais.
        this.fornecedor = "Não informado";
        this.custoDoLote = 0.0;
    }

    // --- MÉTODOS GETTERS ---
    // Fornecem acesso controlado (somente leitura) aos atributos do lote.

    public Produto getProduto() {
        return produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public int getId() {
        return this.id;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public double getCustoDoLote() {
        return custoDoLote;
    }


    public double getQuantidadeInicial() {
        return this.quantidadeInicial;
    }

    // --- MÉTODOS SETTERS ---
    // Permitem a modificação de certos atributos após a criação do objeto.

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setCustoDoLote(double custoDoLote) {
        this.custoDoLote = custoDoLote;
    }

    /**
     * MÉTODO ABSTRATO: Verifica se o lote está vencido.
     * A lógica para determinar se um lote está vencido depende do seu tipo.
     * Um lote perecível terá uma data de validade, um não perecível nunca vence.
     * Ao declarar o método como abstrato, forçamos as subclasses a implementarem
     * essa lógica específica.
     *
     * @return true se o lote estiver vencido, false caso contrário.
     */
    public abstract boolean loteVencido();

    /**
     * MÉTODO ABSTRATO: Verifica se o lote está próximo de vencer.
     * Similar ao `loteVencido()`, a definição de "perto de vencer" só se aplica
     * a lotes perecíveis. As subclasses devem fornecer a implementação.
     *
     * @return true se o lote estiver perto de vencer, false caso contrário.
     */
    public abstract boolean estaPertoDeVencer();

    /**
     * Remove uma determinada quantidade de produto do lote.
     * Contém validações para garantir a consistência dos dados.
     *
     * @param quantidade A quantidade a ser removida.
     * @throws IllegalArgumentException se a quantidade for negativa, zero, ou
     * maior que a quantidade disponível no lote.
     */
    public void removeQuantidade(double quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a remover deve ser maior que zero.");
        }
        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException("Quantidade a remover excede a quantidade disponível no lote.");
        }
        this.quantidade -= quantidade;
    }

    /**
     * Método estático para sincronizar o contador de IDs.
     * Após carregar os dados do sistema a partir de um arquivo, o contador estático
     * `quantidadeDeLotes` estaria zerado. Este método é chamado para atualizá-lo
     * com o maior ID que foi carregado, garantindo que os novos lotes criados
     * não tenham IDs duplicados.
     *
     * @param ultimoId O maior ID encontrado entre os lotes carregados.
     */
    public static void setContadorLotes(int ultimoId) {
        if (ultimoId > quantidadeDeLotes) {
            quantidadeDeLotes = ultimoId;
        }
    }
}