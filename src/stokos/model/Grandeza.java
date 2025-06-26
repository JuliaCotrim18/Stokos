package stokos.model;

/**
 * O enum `Grandeza` define as unidades de medida possíveis para um produto.
 * Ele representa um conjunto fixo e limitado de opções, garantindo que
 * um produto só possa ser medido de uma das formas predefinidas pelo sistema.
 *
 * CONCEITOS DE POO E DESIGN APLICADOS:
 * - Enumeração (Enum): A escolha de um 'enum' em vez de Strings (como "Peso",
 * "Unidade") ou inteiros (1, 2, 3) é uma decisão de design fundamental.
 * - Vantagens:
 * 1. Segurança de Tipo: O compilador impede que um produto seja criado com uma
 * grandeza inválida. Uma variável do tipo `Grandeza` só pode conter os
 * valores `PESO`, `UNIDADE` ou `VOLUME`.
 * 2. Clareza e Legibilidade: O código se torna autoexplicativo. É muito mais
 * claro ler `produto.getGrandeza() == Grandeza.PESO` do que
 * `produto.getGrandeza().equals("Peso")`.
 * 3. Manutenibilidade: Se uma nova unidade de medida for necessária no futuro
 * (por exemplo, `COMPRIMENTO`), ela pode ser adicionada aqui, e o compilador
 * ajudará a identificar onde mais no código essa mudança pode ter impacto.
 */
public enum Grandeza {
    /**
     * Para produtos vendidos por peso.
     * Ex: Carne (kg), Frutas (g).
     */
    PESO,

    /**
     * Para produtos vendidos individualmente, como itens contáveis.
     * Ex: Lâmpada, Garrafa de refrigerante, Pacote de bolachas.
     */
    UNIDADE,

    /**
     * Para produtos vendidos por volume.
     * Ex: Leite (litros), Tinta (ml).
     */
    VOLUME;
}