// ProdutoJaCadastradoException.java

/**
 * Exceção para ser lançada quando se tenta cadastrar um produto
 * que já existe no estoque (ex: mesmo código de barras).
 */
public class ProdutoJaCadastradoException extends Exception {
    public ProdutoJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}

