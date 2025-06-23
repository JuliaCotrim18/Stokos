// TelaProdutos.java (versão com AppContext)

import javax.swing.*;
import java.awt.*;

public class TelaProdutos extends JFrame {
    // --- Atributos de Componentes da UI ---
    private JButton botaoCadastrarNovoProduto;
    private JTextField campoPesquisarProduto;
    private JButton botaoPesquisarProduto;
    private JTextArea areaInfoProduto; // Usando JTextArea para mais flexibilidade
    private JButton botaoAlterarDados;
    private JButton botaoRemoverProduto;
    private JButton botaoVoltar;

    // --- Construtor ---
    public TelaProdutos() {
        super("Stokos - Gerenciamento de Produtos");
        configurarJanela();
        inicializarComponentes();
    }

    // --- Métodos de Configuração da UI ---
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
        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(new BoxLayout(painelNorte, BoxLayout.Y_AXIS));

        botaoCadastrarNovoProduto = new JButton("Cadastrar Novo Produto no Catálogo");
        botaoCadastrarNovoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCadastrarNovoProduto.addActionListener(e -> {
            // A TelaCadastrarProduto também se torna independente
            // Supondo que você terá uma TelaCadastrarProduto refatorada
            // new TelaCadastrarProduto().setVisible(true);
            JOptionPane.showMessageDialog(this, "Funcionalidade 'Cadastrar' a ser implementada.");
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
        areaInfoProduto = new JTextArea();
        areaInfoProduto.setEditable(false);
        areaInfoProduto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaInfoProduto.setText("Pesquise por um produto para ver os detalhes aqui...");
        // Colocamos a área de texto dentro de um painel de rolagem
        JScrollPane scrollPane = new JScrollPane(areaInfoProduto);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        return scrollPane;
    }

    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        botaoAlterarDados = new JButton("Alterar Dados");
        botaoRemoverProduto = new JButton("Remover Produto do Catálogo");
        botaoVoltar = new JButton("Voltar ao Menu");

        // --- LÓGICA DE PERMISSÃO ---
        AppContext app = AppContext.getInstance();
        if (app.getUsuarioLogado().getCargo() != Cargo.CEO) {
            botaoAlterarDados.setEnabled(false);
            botaoRemoverProduto.setEnabled(false);
        }
        
        // Ação para o botão Voltar
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });

        painelSul.add(botaoAlterarDados);
        painelSul.add(botaoRemoverProduto);
        painelSul.add(botaoVoltar);
        return painelSul;
    }
}