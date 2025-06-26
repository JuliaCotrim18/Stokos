package stokos.model;

import java.io.Serializable;
import java.time.LocalDate;
import stokos.model.Produto;

/**
 * Representa uma única transação de venda, como um "recibo".
 * Guarda uma fotografia dos valores no momento da venda.
 */
public class RegistroDeVenda implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String codigoDeBarrasProduto;
    private final String nomeDoProduto;
    private final double quantidadeVendida;
    private final double precoUnitarioNaVenda;
    private final double custoTotalDaVenda;
    private final double lucroDaVenda; // Armazena o lucro já calculado
    private final LocalDate dataDaVenda;

    /**
     * Construtor do RegistroDeVenda.
     * É aqui que o polimorfismo acontece.
     * @param produto O produto vendido (pode ser Comum ou ComImposto).
     * @param quantidade A quantidade vendida na transação.
     * @param custoTotal O custo agregado dos itens retirados dos lotes.
     */
    public RegistroDeVenda(Produto produto, double quantidade, double custoTotal) {
        this.codigoDeBarrasProduto = produto.getCodigoDeBarras();
        this.nomeDoProduto = produto.getNomeDoProduto();
        this.precoUnitarioNaVenda = produto.getPrecoUnitario();
        this.quantidadeVendida = quantidade;
        this.custoTotalDaVenda = custoTotal;
        this.dataDaVenda = LocalDate.now();
        
        // --- A MÁGICA DO POLIMORFISMO ---
        // Chamamos o método calcularLucro() do objeto 'produto'.
        // O Java automaticamente executa a versão correta do método,
        // dependendo se o objeto é um ProdutoComum ou um ProdutoComImposto.
        // A classe RegistroDeVenda não precisa de saber a diferença.
        this.lucroDaVenda = produto.calcularLucro(quantidade, this.precoUnitarioNaVenda, this.custoTotalDaVenda);
    }

    /**
     * Retorna o lucro líquido desta venda, que já foi calculado e armazenado.
     */
    public double getLucroDaVenda() {
        return this.lucroDaVenda;
    }
    
    // Getters existentes para outros atributos
    public String getCodigoDeBarrasProduto() { return codigoDeBarrasProduto; }
    public double getQuantidadeVendida() { return quantidadeVendida; }
}