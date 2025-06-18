// EstoqueVencendoException.java

/**
 * Exceção customizada para ser lançada quando um produto perecível está próximo da data de validade.
 */
public class EstoqueVencendoException extends Exception {

    public EstoqueVencendoException(String message) {
        super(message);
    }
}
