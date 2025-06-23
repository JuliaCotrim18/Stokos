// TelaProdutos.java

import javax.swing.*;
import java.awt.*;

/**
 * Tela principal para o gerenciamento de produtos do catálogo.
 * Permite cadastrar, pesquisar, alterar e remover produtos.
 * O acesso a certas funções é restrito por cargo.
 */
public class TelaProdutos extends JFrame {
    // --- Atributos de Dados ---
    private DadosDoSistema dados;
    private Usuario usuario;

    // --- Atributos de Componentes da UI ---
    // Atributos para que possamos acessá-los depois para adicionar eventos
    private JButton botaoCadastrarNovoProduto;
    private JTextField campoPesquisarProduto;
    private JButton botaoPesquisarProduto;

    // Campos de texto para exibir os dados do produto encontrado
    private JTextField campoId;
    private JTextField campoNome;
    private JTextField campoPreco;
    private JTextField campoCodBarras;
    private JTextField campoCategoria;

    private JButton botaoAlterarDados;
    private JButton botaoRemoverProduto;

    // --- Construtor ---
    public TelaProdutos(DadosDoSistema dados, Usuario usuario) {
        super("Stokos - Gerenciamento de Produtos");
        // Injeta as dependências
        this.dados = dados;
        this.usuario = usuario;

        configurarJanela();
        inicializarComponentes();
    }

    // --- Métodos de Configuração da UI ---
    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE para janelas secundárias
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10)); // Layout principal com espaçamento
    }

    private void inicializarComponentes() {
        // O construtor agora só coordena a montagem.
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(criarPainelNorte(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelCentro(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelSul(), BorderLayout.SOUTH);

        this.add(painelPrincipal);
    }

    private JPanel criarPainelNorte() {
        // Painel que organiza tudo verticalmente
        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(new BoxLayout(painelNorte, BoxLayout.Y_AXIS));

        // Botão de cadastro
        this.botaoCadastrarNovoProduto = new JButton("Cadastrar Novo Produto");
        this.botaoCadastrarNovoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelNorte.add(this.botaoCadastrarNovoProduto);

        // Espaço vertical
        painelNorte.add(Box.createRigidArea(new Dimension(0, 20)));

        // Sub-painel para a pesquisa (itens lado a lado)
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        painelPesquisa.add(new JLabel("Pesquisar por Código:"));
        this.campoPesquisarProduto = new JTextField(25);
        painelPesquisa.add(this.campoPesquisarProduto);
        this.botaoPesquisarProduto = new JButton("Pesquisar");
        painelPesquisa.add(this.botaoPesquisarProduto);

        painelNorte.add(painelPesquisa);
        return painelNorte;
    }

    private JPanel criarPainelCentro() {
        // Painel para exibir os detalhes do produto pesquisado
        JPanel painelCentro = new JPanel();
        // Usamos GridLayout para organizar os campos em formato de formulário
        painelCentro.setLayout(new GridLayout(5, 2, 5, 5)); 
        painelCentro.setBorder(BorderFactory.createTitledBorder("Dados do Produto")); // Borda com título

        // Inicializa os campos de texto, inicialmente não editáveis
        campoId = new JTextField();
        campoId.setEditable(false);
        campoNome = new JTextField();
        campoNome.setEditable(false);
        campoPreco = new JTextField();
        campoPreco.setEditable(false);
        campoCodBarras = new JTextField();
        campoCodBarras.setEditable(false);
        campoCategoria = new JTextField();
        campoCategoria.setEditable(false);
        
        // Adiciona os rótulos e campos ao painel
        painelCentro.add(new JLabel("ID:"));
        painelCentro.add(campoId);
        painelCentro.add(new JLabel("Nome:"));
        painelCentro.add(campoNome);
        painelCentro.add(new JLabel("Preço Unitário:"));
        painelCentro.add(campoPreco);
        painelCentro.add(new JLabel("Cód. Barras:"));
        painelCentro.add(campoCodBarras);
        painelCentro.add(new JLabel("Categoria:"));
        painelCentro.add(campoCategoria);

        return painelCentro;
    }

    private JPanel criarPainelSul() {
        // Painel para os botões de ação na parte inferior
        JPanel painelSul = new JPanel();
        painelSul.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Cria os botões
        this.botaoAlterarDados = new JButton("Alterar Dados");
        this.botaoRemoverProduto = new JButton("Remover Produto do Catálogo");

        // --- LÓGICA DE PERMISSÃO ---
        // Verifica o cargo do usuário logado
        if (usuario.getCargo() != Cargo.CEO) {
            // Se não for CEO, desabilita os botões e adiciona uma dica
            this.botaoAlterarDados.setEnabled(false);
            this.botaoRemoverProduto.setEnabled(false);
            this.botaoAlterarDados.setToolTipText("Acesso restrito ao cargo de CEO.");
            this.botaoRemoverProduto.setToolTipText("Acesso restrito ao cargo de CEO.");
        }
        
        // Adiciona os botões ao painel
        painelSul.add(this.botaoAlterarDados);
        painelSul.add(this.botaoRemoverProduto);

        return painelSul;
    }
}