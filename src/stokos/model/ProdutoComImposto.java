package stokos.model;

/**
 * A classe `ProdutoComImposto` é uma classe CONCRETA que representa um tipo
 * específico de produto: aquele que possui uma alíquota de imposto (ICMS)
 * associada ao seu preço de venda.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Herança: Esta classe `extends Produto`, o que significa que ela herda todos
 * os atributos (como nome, preço, código de barras) e comportamentos (métodos
 * como `getId`, `setPrecoUnitario`) da sua superclasse `Produto`. Isso promove
 * a reutilização de código e estabelece uma relação "é um" (um ProdutoComImposto
 * É UM tipo de Produto).
 * - Sobrescrita (@Override): A classe fornece uma implementação específica para o
 * método `calcularLucro`, que foi definido como abstrato na superclasse `Produto`.
 * Ao sobrescrever este método, a classe adapta o comportamento herdado para
 * atender às suas próprias regras de negócio (neste caso, incluindo o cálculo do imposto).
 * - Polimorfismo: Graças à herança e à sobrescrita, um objeto `ProdutoComImposto`
 * pode ser tratado como se fosse um objeto `Produto`. Por exemplo, em uma lista de
 * `Produto`, podemos ter instâncias de `ProdutoComum` e `ProdutoComImposto`.
 * Quando o método `calcularLucro()` é chamado para um desses objetos, o Java
 * automaticamente executa a versão correta do método (a da subclasse específica),
 * sem que o código que o chamou precise saber qual é o tipo exato do produto.
 */
public class ProdutoComImposto extends Produto {

    // Identificador de versão para a serialização.
    private static final long serialVersionUID = 1L;

    // Atributo específico desta subclasse. Armazena o percentual de ICMS.
    private double percentualIcms;

    /**
     * Construtor da classe `ProdutoComImposto`.
     *
     * @param codigoDeBarras   O código de barras do produto.
     * @param nomeDoProduto    O nome do produto.
     * @param precoUnitario    O preço de venda por unidade.
     * @param grandeza         A unidade de medida do produto (UNIDADE, PESO, etc.).
     * @param percentualIcms   O percentual de ICMS a ser aplicado (ex: 0.18 para 18%).
     */
    public ProdutoComImposto(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza, double percentualIcms) {
        // A primeira linha de um construtor de subclasse DEVE ser a chamada ao
        // construtor da superclasse (`super()`). Isso garante que a parte do objeto
        // que pertence à superclasse (`Produto`) seja inicializada corretamente primeiro.
        super(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);

        // Após a inicialização da parte herdada, inicializamos o atributo
        // específico desta subclasse.
        this.percentualIcms = percentualIcms;
    }

    /**
     * Retorna o percentual de ICMS configurado para este produto.
     * @return o valor do percentual de ICMS.
     */
    public double getPercentualIcms() {
        return percentualIcms;
    }

    /**
     * Permite alterar o percentual de ICMS do produto.
     * @param novoPercentual O novo valor para o percentual de ICMS.
     */
    public void setPercentualIcms(double novoPercentual) {
        this.percentualIcms = novoPercentual;
    }

    /**
     * SOBRESCRITA do método `calcularLucro`.
     * Esta é a implementação específica para produtos com imposto. A anotação `@Override`
     * indica ao compilador que este método pretende sobrescrever um método da superclasse.
     * Isso ajuda a evitar erros, como digitar o nome do método incorretamente.
     *
     * A lógica aqui é diferente da de um `ProdutoComum`: além de subtrair o custo,
     * também subtraímos o valor do imposto calculado sobre a receita.
     *
     * @param quantidade         A quantidade de itens vendidos.
     * @param precoUnitario      O preço de venda por unidade no momento da transação.
     * @param custoTotalDaVenda  O custo total dos itens retirados dos lotes.
     * @return O lucro líquido da transação, já descontado o imposto.
     */
    @Override
    public double calcularLucro(double quantidade, double precoUnitario, double custoTotalDaVenda) {
        // 1. Calcula a receita bruta da venda.
        double receitaTotal = quantidade * precoUnitario;

        // 2. Calcula o valor do imposto com base na receita total e no percentual de ICMS do produto.
        double valorDoImposto = receitaTotal * this.percentualIcms;

        // 3. Calcula o lucro líquido subtraindo o custo e o imposto da receita.
        return receitaTotal - custoTotalDaVenda - valorDoImposto;
    }
}