package stokos.model;

import java.io.Serializable;
import java.util.ArrayList;
import stokos.model.RegistroDeVenda;

/**
 * A classe `HistoricoDeVendas` funciona como um livro de registros para todas as
 * transações de venda concluídas no sistema. Sua principal responsabilidade é
 * armazenar e fornecer acesso a esses registros para fins de consulta e
 * geração de relatórios.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Encapsulamento: A lista de registros (`registros`) é privada e final.
 * - 'private' garante que a lista só pode ser acessada e modificada através
 * dos métodos desta classe (como `adicionarRegistro`), protegendo sua integridade.
 * - 'final' significa que a referência para o objeto `ArrayList` não pode ser
 * alterada após a sua inicialização no construtor, o que torna o estado da
 * classe mais estável e previsível.
 * - Agregação: A classe "tem uma" coleção de objetos `RegistroDeVenda`,
 * demonstrando uma relação de agregação. Ela agrupa e gerencia esses objetos.
 * - Coesão: A classe é altamente coesa, pois todos os seus métodos estão
 * diretamente relacionados à sua única responsabilidade: gerenciar o histórico de vendas.
 * - Serializable: Implementa esta interface para que o histórico completo de vendas
 * possa ser salvo junto com o `DadosDoSistema`.
 */
public class HistoricoDeVendas implements Serializable {

    // Identificador de versão para a serialização.
    private static final long serialVersionUID = 1L;

    // A lista que armazena todos os registros de venda. É 'final' para
    // garantir que a instância da lista nunca seja trocada.
    private final ArrayList<RegistroDeVenda> registros;

    /**
     * Construtor da classe `HistoricoDeVendas`.
     * Inicializa a lista de registros como um `ArrayList` vazio. Isso é
     * fundamental para evitar `NullPointerException` quando métodos como
     * `adicionarRegistro` forem chamados.
     */
    public HistoricoDeVendas() {
        this.registros = new ArrayList<>();
    }

    /**
     * Adiciona um novo registro de venda ao histórico.
     * Este é o único método que permite a inserção de novos dados, centralizando
     * o controle sobre o crescimento do histórico.
     *
     * @param registro O objeto `RegistroDeVenda` a ser adicionado.
     */
    public void adicionarRegistro(RegistroDeVenda registro) {
        this.registros.add(registro);
    }

    /**
     * Calcula e retorna o lucro total acumulado para um produto específico.
     * Este método percorre todo o histórico de vendas, somando o lucro de cada
     * transação que envolve o produto especificado.
     *
     * @param codigoDeBarras O código de barras do produto para o qual o lucro será calculado.
     * @return O valor (double) do lucro total para o produto.
     */
    public double getLucroTotalPorProduto(String codigoDeBarras) {
        double lucroTotal = 0.0;
        // O loop 'for-each' itera sobre a lista encapsulada 'registros'.
        for (RegistroDeVenda registro : registros) {
            // Para cada registro, verifica se pertence ao produto desejado.
            if (registro.getCodigoDeBarrasProduto().equals(codigoDeBarras)) {
                // Se pertencer, acumula o valor do lucro daquela venda específica.
                // Note que a responsabilidade de saber o lucro de uma venda individual
                // é do objeto 'RegistroDeVenda', demonstrando boa separação de responsabilidades.
                lucroTotal += registro.getLucroDaVenda();
            }
        }
        return lucroTotal;
    }

    /**
     * Calcula e retorna a quantidade total vendida de um produto específico.
     * Similar ao método de lucro, este método agrega dados do histórico para
     * fornecer uma informação consolidada, útil para relatórios de vendas.
     *
     * @param codigoDeBarras O código de barras do produto a ser consultado.
     * @return A quantidade total (double) vendida do produto.
     */
    public double getQuantidadeTotalVendida(String codigoDeBarras) {
        double quantidadeTotal = 0.0;
        for (RegistroDeVenda registro : registros) {
            if (registro.getCodigoDeBarrasProduto().equals(codigoDeBarras)) {
                quantidadeTotal += registro.getQuantidadeVendida();
            }
        }
        return quantidadeTotal;
    }
}