package stokos.model;

import stokos.exception.ProdutoNaoCadastradoException;
import stokos.exception.LoteNaoVazioException;
import stokos.exception.ProdutoJaCadastradoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Representa o catálogo central de todos os tipos de produtos que a loja pode comercializar.
 * Esta classe não gerencia o estoque (a quantidade de cada produto), mas sim a definição
 * de cada produto (nome, preço, código de barras, etc.). Funciona como um registro mestre
 * de todos os produtos que existem no sistema.
 *
 * CONCEITOS DE POO APLICADOS:
 * - Encapsulamento: A lista de produtos (`listaDeProdutos`) é um atributo privado.
 * Toda a interação com esta lista é feita através de métodos públicos (`cadastrarProduto`,
 * `removerProduto`, `buscarProduto`), o que protege a integridade dos dados e
 * oculta a complexidade da implementação interna.
 * - Coesão: A classe tem uma responsabilidade única e bem definida: gerenciar o
 * catálogo de produtos. Todos os seus métodos contribuem para este único propósito.
 * - Serializable: Implementa a interface `Serializable` para permitir que o estado do
 * catálogo seja salvo em arquivo, garantindo a persistência dos dados entre
 * as sessões da aplicação.
 */
public class CatalogoDeProdutos implements Serializable {

    private static final long serialVersionUID = 1L;

    // A estrutura de dados `ArrayList` é usada para armazenar a coleção de produtos.
    // Ela é privada para garantir o encapsulamento.
    private ArrayList<Produto> listaDeProdutos;

    /**
     * Construtor padrão da classe CatalogoDeProdutos.
     * Inicializa a lista de produtos como um novo ArrayList vazio, preparando
     * o catálogo para receber novos produtos.
     */
    public CatalogoDeProdutos() {
        this.listaDeProdutos = new ArrayList<>();
    }

    /**
     * Verifica se um produto já existe no catálogo com base no seu código de barras.
     * Este método auxiliar é crucial para evitar a duplicidade de produtos.
     *
     * @param codigoDeBarras O código de barras a ser verificado.
     * @return `true` se um produto com o mesmo código de barras já existir,
     * `false` caso contrário.
     */
    public boolean verificaProdutoCadastrado(String codigoDeBarras) {
        // Utiliza um loop "for-each" para uma leitura mais limpa e segura do que um loop com índice.
        for (Produto produto : listaDeProdutos) {
            if (produto.getCodigoDeBarras().equals(codigoDeBarras)) {
                return true; // Encerra a busca e retorna true assim que encontra uma correspondência.
            }
        }
        return false; // Retorna false se o loop terminar sem encontrar o produto.
    }

    /**
     * Adiciona um novo produto ao catálogo.
     * Antes de adicionar, utiliza o método `verificaProdutoCadastrado` para garantir
     * que não haja outro produto com o mesmo código de barras.
     *
     * @param produto O objeto Produto a ser cadastrado.
     * **POLIMORFISMO DE TIPO:** Este método é um exemplo claro de polimorfismo. Ele aceita
     * qualquer objeto que seja do tipo `Produto` ou de qualquer uma de suas subclasses
     * (como `ProdutoComum` ou `ProdutoComImposto`). A lógica de cadastro não precisa
     * se preocupar com o tipo específico de produto, tornando o sistema extensível a
     * novos tipos de produtos no futuro sem a necessidade de alterar esta classe.
     * @throws ProdutoJaCadastradoException se o código de barras do produto já existir no catálogo.
     */
    public void cadastrarProduto(Produto produto) throws ProdutoJaCadastradoException {
        if (verificaProdutoCadastrado(produto.getCodigoDeBarras())) {
            // O uso de uma exceção customizada torna o código mais expressivo e o tratamento
            // de erros mais específico. Fica claro para o desenvolvedor que o problema
            // não foi um erro genérico, mas sim uma violação da regra de negócio.
            throw new ProdutoJaCadastradoException("Produto já cadastrado: " + produto.getCodigoDeBarras());
        }
        listaDeProdutos.add(produto);
    }

    /**
     * Remove um produto do catálogo com base no seu código de barras.
     *
     * @param codigoDeBarras O código de barras do produto a ser removido.
     * @throws ProdutoNaoCadastradoException se nenhum produto for encontrado com o
     * código de barras fornecido.
     */
    public void removerProduto(String codigoDeBarras, Estoque estoque) throws ProdutoNaoCadastradoException, LoteNaoVazioException {

        // Antes de tudo, verifica se não há lotes do produto no sistema
        double quantidadeDisponivelNoEstoque = estoque.getQuantidadeDisponivel(codigoDeBarras);
        if (quantidadeDisponivelNoEstoque > 0)
        {
            throw new LoteNaoVazioException("Ainda há lotes do produto no estoque!");
        }
        
        // Primeiro, é necessário encontrar a referência do objeto a ser removido.
        Produto produtoParaRemover = null;
        for (Produto p : listaDeProdutos) {
            if (p.getCodigoDeBarras().equals(codigoDeBarras)) {
                produtoParaRemover = p;
                break; // Otimização: para o loop assim que o produto é encontrado.
            }
        }

        // Se, após o loop, a variável 'produtoParaRemover' continuar nula,
        // significa que o produto não foi encontrado.
        if (produtoParaRemover == null) {
            throw new ProdutoNaoCadastradoException("Produto não encontrado: " + codigoDeBarras);
        }

        // Remove o produto da lista usando a referência encontrada.
        listaDeProdutos.remove(produtoParaRemover);
    }

    /**
     * Busca e retorna um produto do catálogo pelo seu código de barras.
     * Este método é fundamental para a interação entre diferentes partes do sistema,
     * como a tela de busca e o módulo de estoque.
     *
     * @param codigoDeBarras O código de barras do produto a ser procurado.
     * @return O objeto `Produto` correspondente, se for encontrado. Caso contrário, retorna `null`.
     */
    public Produto buscarProduto(String codigoDeBarras) {
        for (Produto produto : listaDeProdutos) {
            if (produto.getCodigoDeBarras().equals(codigoDeBarras)) {
                return produto; // Retorna a instância do produto encontrado.
            }
        }
        return null; // Retorna null para indicar que a busca não teve sucesso.
    }

    /**
     * Busca e retorna um produto do catálogo pelo seu ID.
     * Este método é útil para operações que necessitam identificar produtos
     * de forma única, como atualizações ou exclusões.
     *
     * @param id O ID do produto a ser procurado.
     * @return O objeto `Produto` correspondente, se for encontrado. Caso contrário, retorna `null`.
     */
    // Este método é uma sobrecarga do método buscarProduto, permitindo a busca por ID

    public Produto buscarProduto(int id) {
        for (Produto produto : listaDeProdutos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    /**
     * Busca e retorna uma lista de produtos cujo nome contém o termo de busca.
     * A busca é case-insensitive (ignora maiúsculas/minúsculas).
     *
     * @param termoBusca O nome ou parte do nome a ser procurado.
     * @return Uma lista de objetos `Produto` que correspondem ao critério.
     * Retorna uma lista vazia se nenhum produto for encontrado.
     */
    public ArrayList<Produto> buscarProdutosPorNome(String termoBusca) {
        // Usa a API de Streams do Java para uma busca mais limpa e funcional.
        return listaDeProdutos.stream()
                .filter(produto -> produto.getNomeDoProduto().toLowerCase().contains(termoBusca.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Retorna a lista completa de produtos cadastrados.
     * Este método é utilizado principalmente por telas de relatório e visualização
     * que precisam exibir todos os produtos do catálogo.
     *
     * @return um `ArrayList<Produto>` contendo todos os produtos.
     */
    public ArrayList<Produto> getListaDeProdutos() {
        return this.listaDeProdutos;
    }
}