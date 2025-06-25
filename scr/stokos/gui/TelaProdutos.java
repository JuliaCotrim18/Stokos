package stokos.gui;

import javax.swing.*;
import java.awt.*;
import stokos.AppContext; // Para encontrar a classe AppContext
import stokos.model.Cargo; // Para encontrar o Enum Cargo
import stokos.model.Produto;

public class TelaProdutos extends JFrame {
    // --- Atributos de Componentes da UI ---
    private JButton botaoCadastrarNovoProduto;
    private JTextField campoPesquisarProduto;
    private JButton botaoPesquisarProduto;
    
    // Campos de texto individuais para facilitar a edição
    private JTextField campoId, campoNome, campoPreco, campoCodBarras, campoCategoria;
    
    private JButton botaoAlterarDados;
    private JButton botaoRemoverProduto;
    private JButton botaoConfirmarAlteracoes; // Novo botão, inicialmente invisível
    private JButton botaoVoltar;

    // Guarda o produto que está sendo exibido/editado
    private Produto produtoEmExibicao;

    public TelaProdutos() {
        super("Stokos - Gerenciamento de Produtos");
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(criarPainelNorte(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelCentro(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelSul(), BorderLayout.SOUTH);

        this.add(painelPrincipal);
    }

    private JPanel criarPainelNorte() {
        // ... (código do painel norte permanece o mesmo, por enquanto sem ação)
        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(new BoxLayout(painelNorte, BoxLayout.Y_AXIS));
        botaoCadastrarNovoProduto = new JButton("Cadastrar Novo Produto no Catálogo");
        botaoCadastrarNovoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCadastrarNovoProduto.addActionListener(e -> {
            // Aqui você pode abrir a tela de cadastro de produto
            new TelaCadastrarProduto().setVisible(true);
        });


        painelNorte.add(botaoCadastrarNovoProduto);
        painelNorte.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelPesquisa.add(new JLabel("Pesquisar por Código:"));
        campoPesquisarProduto = new JTextField(25);
        painelPesquisa.add(campoPesquisarProduto);
        botaoPesquisarProduto = new JButton("Pesquisar");
        painelPesquisa.add(botaoPesquisarProduto);
        painelNorte.add(painelPesquisa);
        return painelNorte;
    }

    private JScrollPane criarPainelCentro() {
        // Usamos GridLayout para organizar os campos em formato de formulário
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));

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

        painelFormulario.add(new JLabel("ID:"));
        painelFormulario.add(campoId);
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);
        painelFormulario.add(new JLabel("Preço Unitário:"));
        painelFormulario.add(campoPreco);
        painelFormulario.add(new JLabel("Cód. Barras:"));
        painelFormulario.add(campoCodBarras);
        painelFormulario.add(new JLabel("Categoria:"));
        painelFormulario.add(campoCategoria);

        // Coloca o formulário dentro de um painel de rolagem, como você sugeriu
        return new JScrollPane(painelFormulario);
    }

    private JPanel criarPainelSul() {
        // O painel sul agora usa BorderLayout para alinhar os botões
        JPanel painelSul = new JPanel(new BorderLayout());

        // Botão Voltar à esquerda
        botaoVoltar = new JButton("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelSul.add(botaoVoltar, BorderLayout.WEST);

        // Painel para os botões de ação à direita
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botaoAlterarDados = new JButton("Alterar Dados");
        botaoRemoverProduto = new JButton("Remover Produto do Catálogo");
        botaoConfirmarAlteracoes = new JButton("Confirmar Alterações");
        botaoConfirmarAlteracoes.setVisible(false); // Começa invisível

        // --- LÓGICA DE PERMISSÃO ---
        AppContext app = AppContext.getInstance();
        if (app.getUsuarioLogado().getCargo() != Cargo.CEO) {
            botaoAlterarDados.setEnabled(false);
            botaoRemoverProduto.setEnabled(false);
        }
        
        // --- AÇÃO PARA ENTRAR NO "MODO DE EDIÇÃO" ---
        botaoAlterarDados.addActionListener(e -> {
            // Só habilita a edição se um produto estiver sendo exibido
            if (produtoEmExibicao != null) {
                habilitarModoEdicao(true);
            } else {
                JOptionPane.showMessageDialog(this, "Pesquise e selecione um produto antes de alterar.", "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
            }
        });

        painelAcoes.add(botaoAlterarDados);
        painelAcoes.add(botaoRemoverProduto);
        painelAcoes.add(botaoConfirmarAlteracoes);
        
        painelSul.add(painelAcoes, BorderLayout.EAST);
        return painelSul;
    }
    
    /**
     * Habilita ou desabilita o "modo de edição" dos campos do formulário.
     * @param habilitar true para habilitar a edição, false para desabilitar.
     */
    private void habilitarModoEdicao(boolean habilitar) {
        // Apenas os campos que podem ser alterados
        campoNome.setEditable(habilitar);
        campoPreco.setEditable(habilitar);
        campoCodBarras.setEditable(habilitar);
        campoCategoria.setEditable(habilitar);
        
        // Mostra ou esconde o botão de confirmar
        botaoConfirmarAlteracoes.setVisible(habilitar);
        // Desabilita o botão "Alterar" para evitar cliques duplos
        botaoAlterarDados.setEnabled(!habilitar);
    }
}