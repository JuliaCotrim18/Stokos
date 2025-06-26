package stokos.model;

import java.io.Serializable;

// 1. A classe agora é ABSTRATA
public abstract class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Atributos existentes (sem alterações) ---
    private int id;
    private String codigoDeBarras;
    private String categoria;
    private String nomeDoProduto;
    private double quantidadeVendida;
    private double quantidadeDescartada;
    private double precoUnitario;
    private final Grandeza grandeza;
    private double estoqueMinimo;
    private static int contadorProdutos = 0;

    // O construtor continua igual
    public Produto(String codigoDeBarras, String nomeDoProduto, double precoUnitario, Grandeza grandeza) {
        this.id = ++contadorProdutos;
        this.nomeDoProduto = nomeDoProduto;
        this.precoUnitario = precoUnitario;
        this.grandeza = grandeza;
        this.codigoDeBarras = codigoDeBarras;
        this.quantidadeVendida = 0;
        this.quantidadeDescartada = 0;
        this.estoqueMinimo = 0;
    }
    
    // --- NOVO MÉTODO ABSTRATO ---
    /**
     * Calcula o lucro líquido de uma venda específica para este tipo de produto.
     * Cada subclasse (ProdutoComum, ProdutoComImposto) implementará a sua própria lógica.
     * @param quantidade A quantidade de itens vendidos.
     * @param precoUnitario O preço de venda por unidade.
     * @param custoTotalDaVenda O custo total dos itens retirados dos lotes.
     * @return O lucro líquido da transação.
     */
    public abstract double calcularLucro(double quantidade, double precoUnitario, double custoTotalDaVenda);


    // --- Todos os outros getters, setters e métodos continuam exatamente iguais ---

    public static void setContadorProdutos(int ultimoId) {
        if (ultimoId > contadorProdutos) {
            contadorProdutos = ultimoId;
        }
    }

    public static int getTotalProdutosCadastrados() {
        return contadorProdutos;
    }

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

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
    }
    
    public void setEstoqueMinimo(double estoqueMinimo) {
        if (estoqueMinimo < 0) {
            throw new IllegalArgumentException("Estoque mínimo não pode ser negativo.");
        }
        this.estoqueMinimo = estoqueMinimo;
    }

    public void registrarVenda(double quantidadeVendida) {
        if (quantidadeVendida <= 0) {
            throw new IllegalArgumentException("Quantidade vendida deve ser maior que zero.");
        }
        this.quantidadeVendida += quantidadeVendida;
    }

    public double getQuantidadeDescartada() {
        return this.quantidadeDescartada;
    }

    public void registrarDescarte(double quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade descartada deve ser maior que zero.");
        }
        this.quantidadeDescartada += quantidade;
    }
}