package stokos.model;

/**
 * A classe `LoteNaoPerecivel` é uma classe CONCRETA que representa uma remessa
 * de um produto que não tem data de validade.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Herança: Esta classe `extends Lote`, herdando todos os atributos e
 * comportamentos comuns a qualquer lote (como id, produto, quantidade, etc.).
 * Ela especializa a classe `Lote` para um caso de uso específico.
 * - Sobrescrita (@Override): A classe fornece implementações concretas para os
 * métodos abstratos `loteVencido()` e `estaPertoDeVencer()`, definidos na
 * superclasse `Lote`.
 * - Polimorfismo: Um objeto `LoteNaoPerecivel` pode ser tratado como um `Lote`
 * em qualquer parte do sistema (por exemplo, dentro da `listaDeLotes` na classe
 * `Estoque`). Quando os métodos `loteVencido()` ou `estaPertoDeVencer()` são
 * chamados, o Java executa a versão específica implementada aqui, garantindo
 * o comportamento correto sem que o código cliente precise saber o tipo exato do lote.
 */
public class LoteNaoPerecivel extends Lote {

    // Identificador de versão para a serialização.
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe `LoteNaoPerecivel`.
     * Sua principal responsabilidade é invocar o construtor da superclasse `Lote`
     * para inicializar os atributos herdados.
     *
     * @param produto    O objeto Produto ao qual este lote pertence.
     * @param quantidade A quantidade inicial de itens no lote.
     */
    public LoteNaoPerecivel(Produto produto, double quantidade) {
        // A chamada `super()` passa os parâmetros para o construtor da classe pai (`Lote`),
        // que executa a lógica de inicialização comum a todos os lotes (atribuição de ID,
        // produto, quantidade, etc.).
        super(produto, quantidade);
    }


    /**
     * SOBRESCRITA do método `loteVencido`.
     * A anotação `@Override` confirma que estamos fornecendo uma implementação
     * para um método definido na superclasse.
     *
     * Para um lote não perecível, a conceito de "vencido" não se aplica.
     * Portanto, a implementação simplesmente retorna `false`.
     *
     * @return sempre `false`.
     */
    @Override
    public boolean loteVencido() {
        // A lógica é direta: lotes não perecíveis, por definição, nunca vencem.
        return false;
    }

    /**
     * SOBRESCRITA do método `estaPertoDeVencer`.
     * Assim como no método `loteVencido`, um lote que nunca vence também
     * nunca pode estar "perto de vencer".
     *
     * @return sempre `false`.
     */
    @Override
    public boolean estaPertoDeVencer() {
        // A implementação é consistente com a natureza do objeto.
        return false;
    }
}