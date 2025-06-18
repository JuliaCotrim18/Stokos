// EstoqueBaixoException.java

/**
 * Exceção customizada para ser lançada quando um produto atinge o nível de estoque baixo.
 * Isso atende ao requisito de tratamento de exceções. 
 */
public class EstoqueBaixoException extends Exception {

    public EstoqueBaixoException(String message) {
        super(message);
    }
}
