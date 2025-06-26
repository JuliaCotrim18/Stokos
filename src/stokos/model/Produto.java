package stokos.model;

import java.io.Serializable;

/**
 * A classe `Produto` é uma classe ABSTRATA que serve como a superclasse para
 * todos os tipos de produtos no sistema. Ela define um modelo com os atributos
 * e comportamentos comuns a qualquer produto, como ID, nome e preço.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Abstração: Sendo 'abstract', esta classe não pode ser instanciada diretamente.
 * Ela foi projetada para ser herdada por classes concretas (como `ProdutoComum` e
 * `ProdutoComImposto`), que fornecerão implementações para seus comportamentos abstratos.
 * - Herança: Serve como base para a hierarquia de produtos, permitindo que as
 * subclasses reutilizem código e compartilhem uma interface comum.
 * - Encapsulamento: Todos os atributos são 'private', e o acesso a eles é
 * rigorosamente controlado por meio de métodos públicos (getters e setters).
 * Isso protege o estado interno do objeto e garante que as regras de negócio
 * (como não permitir preços negativos) sejam aplicadas.
 * - Polimorfismo: Através do método abstrato `calcularLucro()`, a classe estabelece
 * um contrato que as subclasses devem cumprir. Isso permite que diferentes tipos
 * de produtos sejam tratados de forma uniforme, mas cada um execute seu próprio
 * cálculo de lucro específico.
 * - Serializable: A classe implementa esta interface, o que é essencial para que
 * os objetos `Produto` e suas subclasses possam ser persistidos em arquivo.
 */
public abstract class Produto implements Serializable {

    // Identificador único para controle de versão durante a serialização.
    private static final long serialVersionUID = 1L;

    // --- Atributos ---
    // Atributos privados que definem o estado de um produto.
    private int id;
    private String codigoDeBarras;
    private String categoria;
    private String nomeDoProduto;
    private double quantidadeVendida;
    private double quantidadeDescartada;
    private double precoUnitario;
    private final Grandeza grandeza; // 'final' porque a unidade de medida de um produto não muda.
    private double estoqueMinimo;

    // Contador estático para gerar IDs únicos para cada novo produto.
    // Por ser 'static', pertence à classe, não a uma instância específica.
    private static int contadorProdutos = 0;

    /**
     * Construtor da classe Produto. É chamado pelas subclasses para inicializar
     * os atributos comuns herdados.
     *
     * @param codigoDeBarras O código de identificação único do produto.
     * @param nomeDoProduto  O nome descritivo do produto.
     * @param precoUnitario  O preço de venda do produto.
     * @param grandeza       A unidade de medida (PESO, UNIDADE, VOLUME).
     */
    public Produto(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza) {
        this.id = ++contadorProdutos; // Garante um ID único e sequencial.
        this.nomeDoProduto = nomeDoProduto;
        this.precoUnitario = precoUnitario;
        this.grandeza = grandeza;
        this.codigoDeBarras = codigoDeBarras;
        // Inicializa contadores como zero para um novo produto.
        this.quantidadeVendida = 0;
        this.quantidadeDescartada = 0;
        this.estoqueMinimo = 0;
    }

    /**
     * MÉTODO ABSTRATO: Calcula o lucro líquido de uma venda.
     * Este é o principal ponto de polimorfismo da classe. Ao ser declarado como
     * 'abstract', ele não possui implementação aqui, mas obriga todas as subclasses
     * concretas (`ProdutoComum`, `ProdutoComImposto`) a fornecerem sua própria
     * versão deste método.
     *
     * Assim, quando se chama `produto.calcularLucro()`, o Java decide em tempo de
     * execução qual implementação usar, com base no tipo real do objeto `produto`.
     *
     * @param quantidade         A quantidade de itens vendidos.
     * @param precoUnitario      O preço de venda por unidade.
     * @param custoTotalDaVenda  O custo total dos itens retirados dos lotes.
     * @return O lucro líquido da transação.
     */
    public abstract double calcularLucro(double quantidade, double precoUnitario, double custoTotalDaVenda);


    // --- Métodos Estáticos ---

    /**
     * Método estático para sincronizar o contador de IDs após carregar dados.
     * Quando os produtos são carregados de um arquivo, o contador estático é zerado.
     * Este método o atualiza com o maior ID encontrado, evitando a criação de
     * novos produtos com IDs que já existem.
     *
     * @param ultimoId O maior ID de produto encontrado nos dados carregados.
     */
    public static void setContadorProdutos(int ultimoId) {
        if (ultimoId > contadorProdutos) {
            contadorProdutos = ultimoId;
        }
    }

    /**
     * Retorna o número total de produtos já criados durante a execução do programa.
     * @return o valor do contador estático.
     */
    public static int getTotalProdutosCadastrados() {
        return contadorProdutos;
    }

    // --- Getters e Setters ---
    // Métodos públicos que permitem o acesso e a modificação controlada dos atributos.

    public int getId() {
        return this.id;
    }

    public String getCodigoDeBarras() {
        return this.codigoDeBarras;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public String getNomeDoProduto() {
        return this.nomeDoProduto;
    }

    public double getQuantidadeVendida() {
        return this.quantidadeVendida;
    }

    public double getPrecoUnitario() {
        return this.precoUnitario;
    }

    public Grandeza getGrandeza() {
        return this.grandeza;
    }

    public double getEstoqueMinimo() {
        return this.estoqueMinimo;
    }

    public void setCodigoDeBarras(String codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setNomeDoProduto(String nomeDoProduto) {
        this.nomeDoProduto = nomeDoProduto;
    }

    /**
     * Define o preço unitário, com validação para impedir valores negativos.
     * @param precoUnitario O novo preço de venda.
     * @throws IllegalArgumentException se o preço for negativo.
     */
    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
    }

    /**
     * Define o nível de estoque mínimo para alertas, com validação.
     * @param estoqueMinimo O novo nível de estoque mínimo.
     * @throws IllegalArgumentException se o valor for negativo.
     */
    public void setEstoqueMinimo(double estoqueMinimo) {
        if (estoqueMinimo < 0) {
            throw new IllegalArgumentException("Estoque mínimo não pode ser negativo.");
        }
        this.estoqueMinimo = estoqueMinimo;
    }

    /**
     * Acumula a quantidade vendida de um produto.
     * @param quantidadeVendida A quantidade a ser adicionada ao total vendido.
     */
    public void registrarVenda(double quantidadeVendida) {
        if (quantidadeVendida <= 0) {
            throw new IllegalArgumentException("Quantidade vendida deve ser maior que zero.");
        }
        this.quantidadeVendida += quantidadeVendida;
    }

    public double getQuantidadeDescartada() {
        return this.quantidadeDescartada;
    }

    /**
     * Acumula a quantidade descartada de um produto (ex: por vencimento).
     * @param quantidade A quantidade a ser adicionada ao total descartado.
     */
    public void registrarDescarte(double quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade descartada deve ser maior que zero.");
        }
        this.quantidadeDescartada += quantidade;
    }
}