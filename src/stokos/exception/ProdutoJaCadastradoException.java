package stokos.exception;

/**
 * Representa uma exceção específica para a regra de negócio que impede
 * o cadastro de um produto que já existe no sistema (identificado pelo
 * mesmo código de barras).
 *
 * CONCEITO DE DESIGN: EXCEÇÕES CUSTOMIZADAS
 * Criar uma exceção própria em vez de usar uma genérica (como `Exception` ou
 * `IllegalArgumentException`) torna o código mais claro, legível e robusto.
 * Permite que o código cliente (quem chama o método que lança a exceção)
 * possa capturar e tratar este erro específico de forma diferenciada.
 *
 * Esta é uma "checked exception" porque herda diretamente de `Exception`.
 * Isso obriga o método que a utiliza a declará-la com `throws` ou a tratá-la
 * com um bloco `try-catch`.
 */
public class ProdutoJaCadastradoException extends Exception {

    /**
     * Construtor da exceção.
     *
     * @param mensagem A mensagem de erro detalhada que descreve a causa da exceção.
     * Esta mensagem será útil para debugging e para ser exibida ao usuário.
     */
    public ProdutoJaCadastradoException(String mensagem) {
        // A chamada `super(mensagem)` passa a mensagem de erro para o construtor
        // da classe pai (`Exception`), que se encarrega de armazená-la.
        // A mensagem pode ser posteriormente recuperada com o método getMessage().
        super(mensagem);
    }
}