package stokos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaRelatorio extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTable tabelaRelatorio;
    private DefaultTableModel tableModel; // O "cérebro" da nossa tabela
    private JButton botaoExportar;

    // --- Construtor ---
    public TelaRelatorio() {
        super("Stokos - Relatório de Produtos");

        configurarJanela();
        inicializarComponentes();
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(800, 600); // Relatórios geralmente precisam de mais espaço
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelCentral(), BorderLayout.CENTER);
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

    /**
     * Cria o painel central que contém a JTable com os dados do relatório.
     */
    private JScrollPane criarPainelCentral() {
        // 1. Definir os nomes das colunas
        String[] colunas = {"Nome do Produto", "Qtd. Disponível", "Qtd. Vendida", "Preço Unit.", "Lucro Total Est."};

        // 2. Criar o TableModel com as colunas definidas e 0 linhas iniciais
        tableModel = new DefaultTableModel(colunas, 0);

        // 3. Criar a JTable a partir do nosso modelo
        tabelaRelatorio = new JTable(tableModel);
        
        // Configurações visuais da tabela
        tabelaRelatorio.setFillsViewportHeight(true); // Faz a tabela ocupar toda a altura do scrollpane
        tabelaRelatorio.setRowHeight(25); // Aumenta a altura da linha para melhor leitura
        tabelaRelatorio.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Adicionar dados de exemplo ao modelo
        carregarDadosExemplo();

        // 4. Colocar a JTable dentro de um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabelaRelatorio);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Relatório Geral de Produtos"));

        return scrollPane;
    }

    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoExportar = new JButton("Exportar .csv");
        painelSul.add(botaoExportar);
        return painelSul;
    }

    /**
     * Método de exemplo para popular a tabela. No futuro, os dados virão
     * da análise do nosso sistema.
     */
    private void carregarDadosExemplo() {
        // Criamos um array de Object para cada linha
        Object[] linha1 = {"Leite Integral 1L", 50, 120, 4.50, 540.00};
        Object[] linha2 = {"Pão Francês (un)", 30, 850, 0.50, 425.00};
        Object[] linha3 = {"Refrigerante 2L", 15, 80, 8.00, 640.00};
        Object[] linha4 = {"Carne Moída (kg)", 5.5, 40.2, 35.00, 1407.00};

        // Adicionamos as linhas ao modelo
        tableModel.addRow(linha1);
        tableModel.addRow(linha2);
        tableModel.addRow(linha3);
        tableModel.addRow(linha4);
    }
}