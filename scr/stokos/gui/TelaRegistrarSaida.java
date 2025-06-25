package stokos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TelaRegistrarSaida extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoQuantidade;
    private JTextField campoComprador;
    private JButton botaoRegistrarVenda;
    private JButton botaoRegistrarDescarte;

    // --- Construtor ---
    public TelaRegistrarSaida() {
        super("Registrar Saída de Produto do Estoque");

        configurarJanela();
        inicializarComponentes();
        // A lógica de clique dos botões será adicionada posteriormente.
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(550, 400);
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

        // Linha 2: Comprador
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(new JLabel("Comprador (opcional):"), gbc);

        gbc.gridx = 1;
        campoComprador = new JTextField(20);
        painelFormulario.add(campoComprador, gbc);


        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados da Saída"));
        return scrollPane;
    }

    private JPanel criarPainelSul() {
        // Usamos FlowLayout para alinhar os botões horizontalmente
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centralizado com espaçamento

        botaoRegistrarVenda = new JButton("Registrar Venda");
        botaoRegistrarDescarte = new JButton("Registrar Descarte");

        // Adiciona os botões ao painel
        painelSul.add(botaoRegistrarVenda);
        painelSul.add(botaoRegistrarDescarte);

        return painelSul;
    }
}