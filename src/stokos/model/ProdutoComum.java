package stokos.model;

/**
 * A classe `ProdutoComum` é uma classe CONCRETA que representa o tipo mais
 * básico de produto no sistema: aquele que não possui impostos específicos
 * calculados sobre a venda.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Herança: Esta classe `extends Produto`, herdando todos os atributos e
 * comportamentos da sua superclasse. Ela representa uma especialização da
 * classe `Produto`, seguindo o princípio "é um" (um ProdutoComum É UM
 * tipo de Produto).
 * - Sobrescrita (@Override): Assim como `ProdutoComImposto`, esta classe
 * fornece sua própria implementação do método abstrato `calcularLucro()`.
 * Isso demonstra como diferentes subclasses podem implementar o mesmo
 * "contrato" (definido na superclasse) de maneiras distintas.
 * - Polimorfismo: Objetos `ProdutoComum` podem ser armazenados em coleções
 * do tipo `Produto` e tratados de forma genérica. Quando o método
 * `calcularLucro()` é invocado em um objeto dentro dessa coleção, o Java
 * executará esta versão específica do método se o objeto for uma instância
 * de `ProdutoComum`, demonstrando o poder do polimorfismo.
 */
public class ProdutoComum extends Produto {

    // Identificador de versão para a serialização, herdado implicitamente.
    private static final long serialVersionUID = 1L;

    /**
     * Construtor da classe `ProdutoComum`.
     * Como esta classe não adiciona novos atributos, seu único trabalho é
     * chamar o construtor da superclasse para inicializar os atributos herdados.
     *
     * @param codigoDeBarras   O código de barras do produto.
     * @param nomeDoProduto    O nome do produto.
     * @param precoUnitario    O preço de venda por unidade.
     * @param grandeza         A unidade de medida do produto.
     */
    public ProdutoComum(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza) {
        // A chamada `super()` passa os parâmetros recebidos para o construtor
        // da classe pai (`Produto`), que é responsável por atribuí-los aos
        // campos correspondentes.
        super(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);
    }

    /**
     * SOBRESCRITA do método `calcularLucro`.
     * Esta é a implementação para um produto comum, sem impostos. A anotação
     * `@Override` garante que estamos sobrescrevendo corretamente um método
     * da superclasse.
     *
     * A lógica é a mais simples: o lucro é simplesmente a receita total da
     * venda menos o custo total dos itens vendidos.
     *
     * @param quantidade         A quantidade de itens vendidos.
     * @param precoUnitario      O preço de venda por unidade.
     * @param custoTotalDaVenda  O custo agregado dos itens retirados dos lotes.
     * @return O lucro bruto da transação.
     */
    @Override
    public double calcularLucro(double quantidade, double precoUnitario, double custoTotalDaVenda) {
        // 1. Calcula a receita total da venda.
        double receitaTotal = quantidade * precoUnitario;

        // 2. Retorna a receita subtraída do custo, que representa o lucro.
        return receitaTotal - custoTotalDaVenda;
    }
}