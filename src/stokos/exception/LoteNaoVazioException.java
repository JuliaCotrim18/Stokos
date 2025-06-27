package stokos.exception;

/**
 * Representa uma exceção para a regra de negócio que impede a remoção de um
 * produto do catálogo se ainda existirem lotes não vazios daquele produto no estoque.
 *
 * CONCEITO DE DESIGN: EXCEÇÕES CUSTOMIZADAS
 * Criar esta exceção específica, em vez de usar uma genérica, torna o código
 * mais claro e permite que o erro de "lote não vazio" seja tratado de forma
 * única e adequada pela interface do usuário, por exemplo, exibindo uma
 * mensagem informativa ao invés de um erro genérico.
 */
public class LoteNaoVazioException extends Exception {

    /**
     * Construtor da exceção.
     *
     * @param mensagem A mensagem de erro detalhada que descreve a causa da exceção.
     */
    public LoteNaoVazioException(String mensagem) {
        // A chamada `super(mensagem)` passa a mensagem para o construtor da
        // classe pai (`Exception`), que a armazena para futura recuperação.
        super(mensagem);
    }
}