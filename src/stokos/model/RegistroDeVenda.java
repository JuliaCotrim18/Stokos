package stokos.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa uma única transação de venda, funcionando como um "recibo" ou uma
 * "fotografia" do momento em que a venda ocorreu. Esta classe é projetada para
 * ser imutável, ou seja, uma vez que um registro é criado, seus dados não podem
 * ser alterados. Isso garante a integridade e a confiabilidade do histórico de vendas.
 *
 * CONCEITOS DE POO E DESIGN APLICADOS:
 * - Imutabilidade: Todos os atributos são declarados como `final`. Isso significa
 * que eles devem ser inicializados no construtor e não podem ser modificados
 * depois. Esta é uma prática de design poderosa que torna os objetos mais simples,
 * seguros e fáceis de usar em ambientes complexos.
 * - Delegação e Polimorfismo: O ponto mais crucial desta classe é como ela calcula
 * o lucro. Em vez de ter uma lógica complexa com `if/else` para verificar o tipo
 * de produto, ela simplesmente delega essa responsabilidade ao objeto `Produto`
 * que recebe no construtor. Ela chama `produto.calcularLucro(...)`, e o Java,
 * em tempo de execução, invoca a implementação correta do método (`ProdutoComum`
 * ou `ProdutoComImposto`). Isso demonstra um uso elegante e eficaz do polimorfismo.
 * - Coesão: A classe tem a responsabilidade única e coesa de registrar os dados de
 * uma venda concluída.
 * - Serializable: Essencial para que os registros possam ser persistidos em arquivo.
 */
public class RegistroDeVenda implements Serializable {

    // Identificador de versão para a serialização.
    private static final long serialVersionUID = 1L;

    // --- ATRIBUTOS IMUTÁVEIS ---
    // Todos os campos são 'final' para garantir que um registro de venda
    // não possa ser alterado após sua criação.
    private final String codigoDeBarrasProduto;
    private final String nomeDoProduto;
    private final double quantidadeVendida;
    private final double precoUnitarioNaVenda;
    private final double custoTotalDaVenda;
    private final double lucroDaVenda; // Armazena o lucro já calculado no momento da criação.
    private final LocalDate dataDaVenda;

    /**
     * Construtor do RegistroDeVenda.
     * Este construtor é o coração do polimorfismo no processo de venda.
     * Ele recebe o produto vendido e calcula o lucro com base no tipo real desse produto.
     *
     * @param produto O objeto Produto vendido. Pode ser uma instância de `ProdutoComum` ou `ProdutoComImposto`.
     * @param quantidade A quantidade vendida na transação.
     * @param custoTotal O custo agregado dos itens que foram retirados dos lotes para esta venda.
     */
    public RegistroDeVenda(Produto produto, double quantidade, double custoTotal) {
        // "Fotografa" os dados do produto e da venda no momento da transação.
        this.codigoDeBarrasProduto = produto.getCodigoDeBarras();
        this.nomeDoProduto = produto.getNomeDoProduto();
        this.precoUnitarioNaVenda = produto.getPrecoUnitario();
        this.quantidadeVendida = quantidade;
        this.custoTotalDaVenda = custoTotal;
        this.dataDaVenda = LocalDate.now(); // Registra a data exata da venda.

        // --- A MÁGICA DO POLIMORFISMO ---
        // A classe RegistroDeVenda não sabe e não precisa saber como o lucro é
        // calculado. Ela simplesmente confia no "contrato" definido pela classe
        // abstrata Produto e chama o método calcularLucro().
        //
        // O Java (através da Máquina Virtual Java - JVM) se encarrega de verificar
        // qual é o tipo real do objeto 'produto' em tempo de execução e chamar
        // a versão correta do método:
        // - Se 'produto' for um ProdutoComum, chamará o `calcularLucro` de ProdutoComum.
        // - Se 'produto' for um ProdutoComImposto, chamará o `calcularLucro` de ProdutoComImposto.
        //
        // Isso torna o sistema extensível: se um novo tipo de produto, como
        // 'ProdutoComDesconto', for criado no futuro, esta classe não precisará
        // de NENHUMA alteração para funcionar com ele.
        this.lucroDaVenda = produto.calcularLucro(quantidade, this.precoUnitarioNaVenda, this.custoTotalDaVenda);
    }

    // --- MÉTODOS GETTERS ---
    // Fornecem acesso de leitura aos dados imutáveis do registro.

    /**
     * Retorna o lucro líquido desta venda, que já foi calculado e armazenado.
     * @return o valor do lucro da venda.
     */
    public double getLucroDaVenda() {
        return this.lucroDaVenda;
    }

    public String getCodigoDeBarrasProduto() {
        return codigoDeBarrasProduto;
    }

    public double getQuantidadeVendida() {
        return quantidadeVendida;
    }
}