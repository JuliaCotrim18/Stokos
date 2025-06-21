import java.util.ArrayList;
import java.util.List;

/**
 * A classe Estoque gerencia o conjunto de produtos da loja.
 * Ela centraliza as operações de adição, remoção, atualização e consulta de produtos,
 * além de controlar os níveis de estoque e a validade dos itens.
 */
public class Estoque {

    // Lista para armazenar todos os produtos do estoque.
    private List<Produto> produtos;
    // Nível mínimo para alertar sobre estoque baixo. Pode ser configurado.
    private int nivelMinimoEstoque;

    /**
     * Construtor da classe Estoque.
     * Inicializa a lista de produtos e define um nível mínimo de estoque padrão.
     */
    public Estoque() {
        this.produtos = new ArrayList<>();
        this.nivelMinimoEstoque = 10; // Valor padrão, pode ser alterado com o setter.
    }

    // --- Métodos de Gerenciamento de Produtos ---

    /**
     * Adiciona um novo produto à lista de estoque.
     * @param produto O objeto Produto a ser adicionado.
     */
    public void adicionarProduto(Produto produto) {
        if (produto != null) {
            this.produtos.add(produto);
            System.out.println("SUCESSO: Produto '" + produto.getNomeDoProduto() + "' cadastrado no estoque.");
        } else {
            System.out.println("ERRO: Não é possível adicionar um produto nulo.");
        }
    }

    /**
     * Busca um produto na lista pelo seu ID único.
     * @param id O ID do produto a ser buscado.
     * @return O objeto Produto se encontrado, ou null caso contrário.
     */
    public Produto buscarProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null; // Retorna null se o produto não for encontrado.
    }

    /**
     * Busca um produto na lista pelo seu nome.
     * A busca não diferencia maiúsculas de minúsculas.
     * @param nome O nome do produto a ser buscado.
     * @return O objeto Produto se encontrado, ou null caso contrário.
     */
    public Produto buscarProdutoPorNome(String nome) {
        for (Produto produto : produtos) {
            if (produto.getNomeDoProduto().equalsIgnoreCase(nome)) {
                return produto;
            }
        }
        return null; // Retorna null se o produto não for encontrado.
    }

    /**
     * Atualiza as informações de um produto já existente no estoque.
     * Atualmente, permite alterar o nome e a categoria.
     * @param id O ID do produto a ser atualizado.
     * @param novoNome O novo nome para o produto.
     * @param novaCategoria A nova categoria para o produto.
     */
    public void atualizarProduto(int id, String novoNome, String novaCategoria) {
        Produto produtoParaAtualizar = buscarProdutoPorId(id);
        if (produtoParaAtualizar != null) {
            produtoParaAtualizar.setNomeDoProduto(novoNome);
            produtoParaAtualizar.setCategoria(novaCategoria);
            System.out.println("SUCESSO: Produto ID " + id + " atualizado.");
        } else {
            System.out.println("ERRO: Produto com ID " + id + " não encontrado para atualização.");
        }
    }

    /**
    * Lista todos os produtos presentes no estoque, mostrando os seus detalhes,
    * incluindo informações do fornecedor, caso estejam disponíveis.
    */
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("O estoque está vazio.");
            return;
        }

        System.out.println("\n--- RELATÓRIO DE ESTOQUE ATUAL ---");
        for (Produto produto : produtos) {
            String infoFornecedor = "";
            // Verifica se existe um fornecedor associado ao produto
            if (produto.getFornecedor() != null) {
                infoFornecedor = " | Fornecedor: " + produto.getFornecedor().getNome();
            }
            // Exibe o produto com seus detalhes
            System.out.println(produto.toString() + " | Detalhes: " + produto.getDetalhes() + infoFornecedor);
        }
        System.out.println("-------------------------------------\n");
    }

    // --- Métodos de Movimentação de Estoque ---

    /**
     * Adiciona uma certa quantidade ao estoque de um produto específico.
     * @param id O ID do produto.
     * @param quantidade A quantidade a ser adicionada.
     */
    public void darEntradaEstoque(int id, int quantidade) {
        Produto produto = buscarProdutoPorId(id);
        if (produto != null) {
            produto.adicionaQuantidade(quantidade);
            System.out.println("SUCESSO: " + quantidade + " unidades adicionadas ao produto '" + produto.getNomeDoProduto() + "'.");
        } else {
            System.out.println("ERRO: Produto com ID " + id + " não encontrado para dar entrada.");
        }
    }

    /**
     * Remove uma certa quantidade do estoque de um produto, tratando possíveis alertas.
     * @param id O ID do produto.
     * @param quantidade A quantidade a ser removida.
     * @throws EstoqueBaixoException Se o estoque ficar abaixo do nível mínimo.
     * @throws EstoqueVencendoException Se o produto estiver próximo de vencer.
     */
    public void darBaixaEstoque(int id, int quantidade) throws EstoqueBaixoException, EstoqueVencendoException {
        Produto produto = buscarProdutoPorId(id);
        if (produto != null) {
            produto.removeQuantidade(quantidade);
            System.out.println("SUCESSO: " + quantidade + " unidades removidas do produto '" + produto.getNomeDoProduto() + "'.");
            verificarStatusProduto(produto); // Verifica o status após a remoção.
        } else {
            System.out.println("ERRO: Produto com ID " + id + " não encontrado para dar baixa.");
        }
    }

    /**
     * Registra a venda de um produto, diminuindo sua quantidade em estoque.
     * @param id O ID do produto vendido.
     * @param quantidade A quantidade vendida.
     * @throws EstoqueBaixoException Se o estoque ficar abaixo do nível mínimo após a venda.
     * @throws EstoqueVencendoException Se o produto vendido estiver próximo de vencer.
     */
    public void registrarVenda(int id, int quantidade) throws EstoqueBaixoException, EstoqueVencendoException {
        Produto produto = buscarProdutoPorId(id);
        if (produto != null) {
            produto.vendeQuantidade(quantidade);
            System.out.println("VENDA REGISTRADA: " + quantidade + " unidades de '" + produto.getNomeDoProduto() + "'.");
            verificarStatusProduto(produto); // Verifica o status após a venda.
        } else {
            System.out.println("ERRO: Produto com ID " + id + " não encontrado para registrar venda.");
        }
    }

    // --- Métodos de Controle e Configuração ---

    /**
     * Verifica o status de um produto (nível de estoque e validade) e lança exceções se necessário.
     * @param produto O produto a ser verificado.
     * @throws EstoqueBaixoException Se a quantidade disponível for menor que o nível mínimo.
     * @throws EstoqueVencendoException Se o produto for perecível e estiver perto de vencer.
     */
    private void verificarStatusProduto(Produto produto) throws EstoqueBaixoException, EstoqueVencendoException {
        // 1. Alerta de Estoque Baixo
        if (produto.getQuantidadeDisponivel() > 0 && produto.getQuantidadeDisponivel() < this.nivelMinimoEstoque) {
            throw new EstoqueBaixoException(
                "ALERTA: Estoque baixo para o produto '" + produto.getNomeDoProduto() +
                "'. Quantidade restante: " + produto.getQuantidadeDisponivel()
            );
        }

        // 2. Alerta de Produto Vencendo (apenas para perecíveis)
        if (produto instanceof ProdutoPerecivel) {
            ProdutoPerecivel perecivel = (ProdutoPerecivel) produto;
            // Alerta se faltarem 7 dias ou menos para o vencimento.
            if (perecivel.getDiasParaVencimento() <= 7 && perecivel.getDiasParaVencimento() >= 0) {
                throw new EstoqueVencendoException(
                    "ALERTA: O produto '" + perecivel.getNomeDoProduto() +
                    "' está próximo da data de validade! Vence em " + perecivel.getDiasParaVencimento() + " dias."
                );
            }
        }
    }

    /**
     * Permite ajustar o nível mínimo de estoque para disparar o alerta.
     * @param nivelMinimo O novo valor para o nível mínimo de estoque.
     */
    public void setNivelMinimoEstoque(int nivelMinimo) {
        if (nivelMinimo > 0) {
            this.nivelMinimoEstoque = nivelMinimo;
            System.out.println("Nível mínimo de estoque configurado para: " + nivelMinimo);
        } else {
            System.out.println("ERRO: O nível mínimo de estoque deve ser maior que zero.");
        }
    }
}
