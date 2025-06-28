package stokos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import stokos.AppContext;
import stokos.model.CatalogoDeProdutos;
import stokos.model.Estoque;
import stokos.model.HistoricoDeVendas;
import stokos.model.Produto;
import stokos.service.ServicoDeExportacao;
import stokos.Config;

/**
 * Representa a tela de Relatórios, que exibe uma visão consolidada do
 * desempenho de cada produto, combinando dados de estoque, vendas e lucro.
 * A tela também oferece a funcionalidade de exportar o relatório para um arquivo CSV.
 */
public class TelaRelatorios extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTable tabelaRelatorio; // Componente para exibir o relatório.
    private DefaultTableModel tableModel; // Modelo de dados que alimenta a JTable.
    private JButton botaoExportar;

    /**
     * Construtor da tela de Relatórios.
     */
    public TelaRelatorios() {
        super("Stokos - Relatório de Produtos");
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades essenciais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Inicializa e organiza os painéis principais dentro da janela.
     */
    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelCentral(), BorderLayout.CENTER);
        this.add(criarPainelSul(), BorderLayout.SOUTH);
    }

    /**
     * Cria o painel superior (Norte) com o botão de navegação "Voltar".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel central que contém a tabela do relatório.
     * @return Um JScrollPane contendo a JTable.
     */
    private JScrollPane criarPainelCentral() {
        // ALTERADO: Adicionada a coluna "Qtd. Descartada"
        String[] colunas = {"Nome do Produto", "Qtd. Disponível", "Qtd. Vendida", "Qtd. Descartada", "Preço Unit.", "Lucro Total Est."};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaRelatorio = new JTable(tableModel);

        tabelaRelatorio.setFillsViewportHeight(true);
        tabelaRelatorio.setRowHeight(25);
        tabelaRelatorio.setFont(new Font("Arial", Font.PLAIN, 12));

        carregarDadosDoRelatorio();

        JScrollPane scrollPane = new JScrollPane(tabelaRelatorio);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Relatório Geral de Produtos"));

        return scrollPane;
    }

    /**
     * Cria o painel inferior (Sul) com o botão de ação "Exportar .csv".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoExportar = new JButton("Exportar .csv");

        // Adiciona a lógica para exportar os dados da tabela.
        botaoExportar.addActionListener(e -> {
            try {
                // Instancia o serviço de exportação para realizar a tarefa.
                ServicoDeExportacao servico = new ServicoDeExportacao();
                // Delega a responsabilidade de exportar, passando o modelo da tabela e o caminho do arquivo.
                servico.exportarParaCSV(tableModel, Config.CAMINHO_SAIDA_ARQUIVO_CSV);

                JOptionPane.showMessageDialog(this,
                    "Relatório exportado com sucesso para:\n" + Config.CAMINHO_SAIDA_ARQUIVO_CSV,
                    "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                // Trata possíveis erros de escrita no arquivo.
                JOptionPane.showMessageDialog(this,
                    "Ocorreu um erro ao exportar o arquivo:\n" + ex.getMessage(),
                    "Erro de exportação", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelSul.add(botaoExportar);
        return painelSul;
    }

    /**
     * Busca e consolida os dados de diferentes partes do sistema para preencher o relatório.
     */
    private void carregarDadosDoRelatorio() {
        tableModel.setRowCount(0); // Limpa a tabela antes de popular com novos dados.

        // Acessa os dados necessários através do AppContext.
        AppContext app = AppContext.getInstance();
        CatalogoDeProdutos catalogo = app.getDados().catalogo;
        Estoque estoque = app.getDados().estoque;
        HistoricoDeVendas historico = app.getDados().historicoDeVendas;

        // Itera sobre cada produto do catálogo para criar uma linha de relatório.
        for (Produto produto : catalogo.getListaDeProdutos()) {
            String codigo = produto.getCodigoDeBarras();

            // Cria um array de Object para representar os dados de uma única linha.
            // Cada informação é buscada da respectiva classe de modelo responsável.
            Object[] linha = {
                produto.getNomeDoProduto(),
                estoque.getQuantidadeDisponivel(codigo),
                historico.getQuantidadeTotalVendida(codigo),
                produto.getQuantidadeDescartada(),
                produto.getPrecoUnitario(),
                String.format("%.2f", historico.getLucroTotalPorProduto(codigo)) // Formata o lucro para duas casas decimais.
            };

            // Adiciona a linha consolidada ao modelo da tabela.
            tableModel.addRow(linha);
        }
    }
}