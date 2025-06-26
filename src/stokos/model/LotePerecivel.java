package stokos.model;

import java.time.LocalDate;

/**
 * A classe `LotePerecivel` é uma classe CONCRETA que representa uma remessa
 * de um produto que possui uma data de validade. É uma especialização da
 * classe abstrata `Lote`.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Herança: `extends Lote`, herdando todos os atributos e comportamentos
 * comuns a qualquer lote. Adiciona o atributo específico `dataDeValidade`.
 * - Sobrescrita (@Override): Fornece implementações lógicas para os métodos
 * abstratos `loteVencido()` e `estaPertoDeVencer()`, que foram definidos
 * como um "contrato" na superclasse.
 * - Polimorfismo: Objetos `LotePerecivel` são tratados como `Lote` em todo o
 * sistema (por exemplo, na lista de estoque). Quando métodos como
 * `loteVencido()` são chamados, o Java executa a versão específica desta
 * classe, permitindo que o sistema lide com diferentes tipos de lote de
 * forma transparente e correta.
 */
public class LotePerecivel extends Lote {

    // Identificador de versão para a serialização.
    private static final long serialVersionUID = 1L;

    /**
     * Atributo final para armazenar a data de validade.
     * Sendo 'final', garante que a data de validade de um lote, uma vez definida,
     * não pode ser alterada, o que é crucial para a integridade do controle de estoque.
     */
    private final LocalDate dataDeValidade;

    /**
     * Construtor da classe `LotePerecivel`.
     *
     * @param produto          O objeto Produto ao qual este lote pertence.
     * @param quantidadeInicial A quantidade de itens na remessa.
     * @param dataDeValidade   A data de validade específica deste lote.
     */
    public LotePerecivel(Produto produto, double quantidadeInicial, LocalDate dataDeValidade) {
        // 1. Chama o construtor da superclasse `Lote` para inicializar os atributos comuns.
        super(produto, quantidadeInicial);
        // 2. Inicializa o atributo específico desta subclasse.
        this.dataDeValidade = dataDeValidade;
    }

    /**
     * Retorna a data de validade do lote.
     * @return um objeto `LocalDate` com a data de validade.
     */
    public LocalDate getDataDeValidade() {
        return dataDeValidade;
    }

    /**
     * Calcula o número de dias restantes até a data de validade.
     * Utiliza a API de Data e Hora do Java (`java.time`) para um cálculo preciso.
     *
     * @return o número de dias até o vencimento. Pode retornar um valor negativo
     * se a data de validade já passou.
     */
    public int diasAteVencer() {
        // O método `until` calcula o período entre a data atual (`LocalDate.now()`)
        // e a data de validade, e `getDays()` extrai o número de dias desse período.
        return (int) LocalDate.now().until(dataDeValidade).getDays();
    }

    /**
     * SOBRESCRITA do método `loteVencido`.
     * Implementa a lógica para verificar se a data de validade já foi ultrapassada.
     *
     * @return `true` se a data atual for posterior à data de validade; `false` caso contrário.
     */
    @Override
    public boolean loteVencido() {
        return LocalDate.now().isAfter(dataDeValidade);
    }

    /**
     * SOBRESCRITA do método `estaPertoDeVencer`.
     * Implementa a lógica de negócio para alertar sobre lotes que estão próximos
     * do vencimento, com base em um limite de dias configurado no sistema.
     *
     * @return `true` se o lote não estiver vencido e o número de dias para vencer
     * for menor ou igual ao limite do sistema; `false` caso contrário.
     */
    @Override
    public boolean estaPertoDeVencer() {
        // Primeira verificação: um lote que já venceu não está "perto de vencer".
        // Isso evita que lotes vencidos apareçam em alertas de "proximidade de vencimento".
        if (loteVencido()) {
            return false;
        }
        // Compara os dias restantes com o valor configurado na classe `Config`.
        // Isso torna o sistema flexível, pois o limite de dias para o alerta
        // pode ser alterado em um único local (`stokos.Config`), sem precisar
        // modificar esta classe.
        return diasAteVencer() <= stokos.Config.DIAS_PARA_ESTAR_PROXIMO_DO_VENCIMENTO;
    }
}