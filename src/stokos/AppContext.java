package stokos;

import stokos.service.*;
import stokos.persistence.*;
import stokos.model.*;

/**
 * A classe AppContext implementa o padrão de projeto Singleton.
 * Sua finalidade é centralizar e gerenciar o estado global e os serviços da
 * aplicação, garantindo que exista apenas uma única instância desses recursos
 * acessível de qualquer parte do código.
 *
 * Funciona como um "hub" central que conecta a camada de interface gráfica (GUI)
 * com a camada de dados (model) e de serviços (service).
 */
public class AppContext {
    // A instância única é criada no momento em que a classe é carregada pela JVM.
    // 'private static final' garante que ela seja única e imutável.
    private static final AppContext instance = new AppContext();

    // Atributos que representam os serviços e dados globais da aplicação.
    private ServicoDeArmazenamento servicoDeArmazenamento;
    private ServicoDeAutenticacao servicoDeAutenticacao;
    private DadosDoSistema dados;
    private Usuario usuarioLogado; // Guarda o usuário da sessão atual.

    /**
     * Construtor privado para impedir a criação de novas instâncias da classe.
     * Esta é a característica fundamental do padrão Singleton.
     * O construtor é chamado apenas uma vez, quando a 'instance' é criada.
     */
    private AppContext() {
        this.servicoDeAutenticacao = new ServicoDeAutenticacao();
        this.servicoDeArmazenamento = new ArmazenamentoEmArquivo(stokos.Config.CAMINHO_ARMAZENAMENTO);
        // Carrega os dados persistidos logo na inicialização do contexto.
        carregarDados();
    }

    /**
     * Método público e estático que fornece o ponto de acesso global à
     * instância única da classe.
     *
     * @return A única instância de AppContext.
     */
    public static AppContext getInstance() {
        return instance;
    }

    /**
     * Carrega os dados do sistema a partir do serviço de armazenamento.
     * Inclui lógicas de segurança para garantir que, mesmo em caso de falha ou
     * ausência de um arquivo de dados, a aplicação inicie com um estado válido.
     */
    public void carregarDados() {
        try {
            this.dados = servicoDeArmazenamento.carregarDados();
            if (this.dados == null) { // Se o arquivo de dados não existe...
                this.dados = new DadosDoSistema(); // ...cria um novo objeto de dados vazio.
            }
        } catch (Exception e) {
            System.err.println("Falha ao carregar dados, iniciando com sistema novo.");
            this.dados = new DadosDoSistema(); // Em caso de erro, também inicia um sistema novo.
        }

        // Garante que os componentes principais nunca sejam nulos após o carregamento.
        if (this.dados.catalogo == null) this.dados.catalogo = new CatalogoDeProdutos();
        if (this.dados.estoque == null) this.dados.estoque = new Estoque(this.dados.catalogo);
        if (this.dados.historicoDeVendas == null) this.dados.historicoDeVendas = new HistoricoDeVendas();

        // Sincroniza os contadores estáticos para evitar IDs duplicados.
        sincronizarContadorDeLotes();
        sincronizarContadorProdutos();
    }

    /**
     * Delega a tarefa de salvar os dados do sistema para o serviço de armazenamento.
     * É chamado pelo "Shutdown Hook" na classe Main.
     */
    public void salvarDados() {
        try {
            servicoDeArmazenamento.salvarDados(this.dados);
        } catch (Exception e) {
            System.err.println("Falha crítica ao salvar os dados.");
        }
    }
    
    /**
     * Sincroniza o contador estático de Lotes.
     * Após carregar os dados, este método percorre os lotes existentes, encontra o
     * maior ID e atualiza o contador estático na classe Lote. Isso previne que
     * novos lotes sejam criados com IDs que já existem.
     */
    private void sincronizarContadorDeLotes() {
        int maxId = 0;
        if (this.dados.estoque != null && this.dados.estoque.getLotes() != null) {
            for (Lote lote : this.dados.estoque.getLotes()) {
                if (lote.getId() > maxId) {
                    maxId = lote.getId();
                }
            }
        }
        Lote.setContadorLotes(maxId);
    }

    /**
     * Sincroniza o contador estático de Produtos.
     * Funciona de forma análoga ao `sincronizarContadorDeLotes`, garantindo a
     * unicidade dos IDs dos novos produtos.
     */
    private void sincronizarContadorProdutos() {
        int maxId = 0;
        if (this.dados.catalogo != null && this.dados.catalogo.getListaDeProdutos() != null) {
            for (Produto produto : this.dados.catalogo.getListaDeProdutos()) {
                if (produto.getId() > maxId) {
                    maxId = produto.getId();
                }
            }
        }
        Produto.setContadorProdutos(maxId);
    }

    // --- Getters e Setters ---
    // Fornecem acesso controlado aos serviços e dados gerenciados pelo AppContext.

    public ServicoDeAutenticacao getServicoDeAutenticacao() { return servicoDeAutenticacao; }
    public DadosDoSistema getDados() { return dados; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuario) { this.usuarioLogado = usuario; }
}