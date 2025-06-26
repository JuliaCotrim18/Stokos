package stokos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import stokos.AppContext;
import stokos.model.CatalogoDeProdutos;
import stokos.model.Estoque;
import stokos.model.HistoricoDeVendas;
import stokos.model.Produto;

public class TelaRelatorios extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTable tabelaRelatorio;
    private DefaultTableModel tableModel; // O "cérebro" da nossa tabela
    private JButton botaoExportar;

    // --- Construtor ---
    public TelaRelatorios() {
        super("Stokos - Relatório de Produtos");

        configurarJanela();
        inicializarComponentes();
        // A chamada para carregar os dados agora é feita apenas uma vez, dentro de criarPainelCentral()
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(800, 600);
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
        // Adicionando a ação para o botão voltar, que estava faltando
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    private JScrollPane criarPainelCentral() {
        String[] colunas = {"Nome do Produto", "Qtd. Disponível", "Qtd. Vendida", "Preço Unit.", "Lucro Total Est."};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaRelatorio = new JTable(tableModel);
        
        tabelaRelatorio.setFillsViewportHeight(true);
        tabelaRelatorio.setRowHeight(25);
        tabelaRelatorio.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Carrega os dados reais aqui, depois que a tabela já foi criada
        carregarDadosDoRelatorio();

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
     * Carrega os dados reais do sistema para preencher a tabela.
     */
    private void carregarDadosDoRelatorio() {
        tableModel.setRowCount(0); // Limpa quaisquer dados antigos

        AppContext app = AppContext.getInstance();
        CatalogoDeProdutos catalogo = app.getDados().catalogo;
        Estoque estoque = app.getDados().estoque;
        HistoricoDeVendas historico = app.getDados().historicoDeVendas;

        for (Produto produto : catalogo.getListaDeProdutos()) {
            String codigo = produto.getCodigoDeBarras();

            Object[] linha = {
                produto.getNomeDoProduto(),
                estoque.getQuantidadeDisponivel(codigo),
                historico.getQuantidadeTotalVendida(codigo),
                produto.getPrecoUnitario(),
                String.format("%.2f", historico.getLucroTotalPorProduto(codigo))
            };

            tableModel.addRow(linha);
        }
    }
}