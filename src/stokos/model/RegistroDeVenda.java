package stokos.model;

import java.io.Serializable;
import java.time.LocalDate;
import stokos.model.Produto; // Importação correta

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
    private final LocalDate dataDaVenda;

    // O construtor está correto, o erro era pelo nome do arquivo
    public RegistroDeVenda(Produto produto, double quantidade, double custoTotal) {
        this.codigoDeBarrasProduto = produto.getCodigoDeBarras();
        this.nomeDoProduto = produto.getNomeDoProduto();
        this.precoUnitarioNaVenda = produto.getPrecoUnitario();
        this.quantidadeVendida = quantidade;
        this.custoTotalDaVenda = custoTotal;
        this.dataDaVenda = LocalDate.now();
    }

    public double getLucroDaVenda() {
        double receitaTotal = this.precoUnitarioNaVenda * this.quantidadeVendida;
        return receitaTotal - this.custoTotalDaVenda;
    }

    public String getCodigoDeBarrasProduto() { return codigoDeBarrasProduto; }
    public double getQuantidadeVendida() { return quantidadeVendida; }
}