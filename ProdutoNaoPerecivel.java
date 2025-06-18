// ProdutoNaoPerecivel.java


public class ProdutoNaoPerecivel extends Produto {

    // Construtor padrão
    public ProdutoNaoPerecivel(String nomeDoProduto, double precoUnitario, Grandeza grandeza, Fornecedor fornecedor) {
        super(nomeDoProduto, precoUnitario, grandeza, fornecedor);
    }
    
    // Construtor sobrecarregado : se o fornecedor não for necessário, podemos ter um construtor que não o recebe
    public ProdutoNaoPerecivel(String nomeDoProduto, double precoUnitario, Grandeza grandeza) {
        super(nomeDoProduto, precoUnitario, grandeza);
    }

    @Override
    public String getDetalhes() {
        return "Este produto não possui data de validade.";
    }
}