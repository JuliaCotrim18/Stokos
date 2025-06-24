import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaAdicionarLote extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoQuantidade;
    private JTextField campoFornecedor;
    private JTextField campoCusto;
    private JRadioButton radioPerecivel;
    private JLabel labelDataValidade; // Rótulo para o campo que aparece/desaparece
    private JTextField campoDataValidade; // Campo que aparece/desaparece
    private JButton botaoAdicionar;

    // --- Construtor ---
    public TelaAdicionarLote() {
        super("Adicionar Novo Lote ao Estoque");

        configurarJanela();
        inicializarComponentes();
        adicionarListeners(); // Adiciona a lógica de interação
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(550, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelFormularioScrollable(), BorderLayout.CENTER);
        this.add(criarPainelSul(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> 
        {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    private JScrollPane criarPainelFormularioScrollable() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Código de Barras
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(new JLabel("Código de Barras do Produto:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campoCodigoBarras = new JTextField(20);
        painelFormulario.add(campoCodigoBarras, gbc);

        // Linha 1: Quantidade
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        campoQuantidade = new JTextField(20);
        painelFormulario.add(campoQuantidade, gbc);

        // Linha 2: Fornecedor
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1;
        campoFornecedor = new JTextField(20);
        painelFormulario.add(campoFornecedor, gbc);

        // Linha 3: Custo
        gbc.gridx = 0;
        gbc.gridy = 3;
        painelFormulario.add(new JLabel("Custo (Total do Lote):"), gbc);
        gbc.gridx = 1;
        campoCusto = new JTextField(20);
        painelFormulario.add(campoCusto, gbc);

        // Linha 4: Lote Perecível (RadioButton)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupa duas colunas
        radioPerecivel = new JRadioButton("Este lote é perecível?");
        painelFormulario.add(radioPerecivel, gbc);

        // Linha 5: Data de Validade (inicialmente invisível)
        gbc.gridy = 5;
        gbc.gridwidth = 1; // Volta a ocupar uma coluna
        gbc.gridx = 0;
        labelDataValidade = new JLabel("Data de Validade (dd/mm/aaaa):");
        painelFormulario.add(labelDataValidade, gbc);

        gbc.gridx = 1;
        campoDataValidade = new JTextField(20);
        painelFormulario.add(campoDataValidade, gbc);

        // Esconde os campos de data de validade por padrão
        labelDataValidade.setVisible(false);
        campoDataValidade.setVisible(false);

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados do Lote"));
        return scrollPane;
    }

    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoAdicionar = new JButton("Adicionar Lote");
        botaoAdicionar.setPreferredSize(new Dimension(150, 30));
        painelSul.add(botaoAdicionar);
        return painelSul;
    }

    /**
     * Adiciona os 'listeners' para os componentes interativos da janela.
     */
    private void adicionarListeners() {
        radioPerecivel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se o radio button está selecionado
                boolean selecionado = radioPerecivel.isSelected();

                // Altera a visibilidade dos componentes de data de validade
                labelDataValidade.setVisible(selecionado);
                campoDataValidade.setVisible(selecionado);
            }
        });
    }
}