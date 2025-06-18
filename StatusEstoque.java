// StatusEstoque.java

/**
 * Enum para representar o status de um produto no estoque.
 * Facilita a criação de alertas e relatórios. 
 */
public enum StatusEstoque {
    OK,
    BAIXO, // Estoque atingiu o nível mínimo de alerta
    CRITICO; // Estoque muito baixo ou zerado
}