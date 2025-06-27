package stokos.exception;

/**
 * Representa uma exceção para a regra de negócio que impede a conclusão de uma
 * venda se a quantidade solicitada de um produto for maior que a quantidade
 * disponível no estoque.
 *
 * CONCEITO DE DESIGN: EXCEÇÕES CUSTOMIZADAS
 * O uso de uma exceção específica para este cenário é fundamental. Permite que a
 * camada de serviço (onde a venda é registrada) comunique de forma clara à
* camada de interface (a tela de vendas) que o problema não foi um erro
 * técnico, mas sim uma falha na validação da regra de negócio. A interface pode
 * então capturar este erro específico e exibir uma mensagem amigável ao usuário.
 */
public class QuantidadeInsuficienteException extends Exception {

    /**
     * Construtor da exceção.
     *
     * @param mensagem A mensagem de erro detalhada, que pode incluir qual produto
     * está em falta e a quantidade disponível.
     */
    public QuantidadeInsuficienteException(String mensagem) {
        // A chamada `super(mensagem)` passa a mensagem para o construtor da
        // classe pai (`Exception`), que a armazena para futura recuperação.
        super(mensagem);
    }
}