// ProdutoNaoCadastradoException.java

/**
 * Exceção para ser lançada quando se tenta interagir com um produto que ainda não está cadastrado
 **/
public class ProdutoNaoCadastradoException extends Exception {
    public ProdutoNaoCadastradoException(String mensagem) {
        super(mensagem);
    }
}

