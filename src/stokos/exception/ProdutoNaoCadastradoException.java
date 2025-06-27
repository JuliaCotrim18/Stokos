package stokos.exception;

/**
 * Representa uma exceção para a regra de negócio que ocorre quando se tenta
 * realizar uma operação (como adicionar um lote ou registrar uma venda) com um
 * produto que não existe no catálogo do sistema.
 *
 * CONCEITO DE DESIGN: EXCEÇÕES CUSTOMIZADAS
 * Esta é uma das exceções mais importantes do sistema. Usá-la em vez de uma
 * exceção genérica (como `NullPointerException`) é crucial porque ela carrega
 * um significado de negócio claro: "a operação falhou porque o produto
 * informado é desconhecido". Isso permite que a interface gráfica capture
 * este erro específico e informe o usuário de forma precisa, como "Produto
 * não encontrado. Verifique o código de barras.".
 */
public class ProdutoNaoCadastradoException extends Exception {

    /**
     * Construtor da exceção.
     *
     * @param mensagem A mensagem de erro detalhada que descreve a causa da exceção.
     */
    public ProdutoNaoCadastradoException(String mensagem) {
        // A chamada `super(mensagem)` passa a mensagem para o construtor da
        // classe pai (`Exception`), que a armazena para futura recuperação.
        super(mensagem);
    }
}