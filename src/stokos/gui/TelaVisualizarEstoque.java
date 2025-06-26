package stokos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import stokos.AppContext;
import stokos.model.Lote;
import stokos.model.LotePerecivel;

public class TelaVisualizarEstoque extends JFrame {

    // --- Atributos da UI ---
    private JTable tabelaEstoque;
    private DefaultTableModel tableModel;
    private JButton botaoVoltar;

    // --- Construtor ---
    public TelaVisualizarEstoque() {
        super("Stokos - Visualização de Estoque");

        configurarJanela();
        inicializarComponentes();
        carregarDadosDaTabela();
    }

    // --- Configurações da Janela ---
    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // --- Inicialização dos Componentes ---
    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelCentral(), BorderLayout.CENTER);
    }

    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            new TelaEstoque().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    private JScrollPane criarPainelCentral() {
        // 1. Definir os nomes das colunas da tabela
        String[] colunas = {
            "ID do Lote", "Produto", "Quantidade", "Fornecedor", 
            "Custo Total", "Perecível?", "Dias até Vencer"
        };

        // 2. Criar o modelo da tabela, inicialmente sem linhas
        tableModel = new DefaultTableModel(colunas, 0);

        // 3. Criar a JTable com o modelo
        tabelaEstoque = new JTable(tableModel);
        
        // Configurações visuais da tabela para melhor leitura
        tabelaEstoque.setRowHeight(25);
        tabelaEstoque.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaEstoque.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // 4. Colocar a tabela dentro de um painel com barra de rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lotes Atuais no Estoque"));

        return scrollPane;
    }

    /**
     * Busca os dados do estoque no AppContext e popula a tabela.
     */
    private void carregarDadosDaTabela() {
        AppContext app = AppContext.getInstance();
        ArrayList<Lote> lotes = app.getDados().estoque.getLotes();

        // Opcional: Ordenar a lista por ID para uma visualização mais consistente
        lotes.sort(Comparator.comparingInt(Lote::getId));

        for (Lote lote : lotes) {
            Object[] linha = new Object[7];

            linha[0] = lote.getId();
            linha[1] = lote.getProduto().getNomeDoProduto();
            linha[2] = lote.getQuantidade();
            linha[3] = lote.getFornecedor();
            linha[4] = lote.getCustoDoLote();

            // Verifica se o lote é perecível para preencher as últimas colunas
            if (lote instanceof LotePerecivel) {
                LotePerecivel loteP = (LotePerecivel) lote;
                linha[5] = "Sim";
                linha[6] = loteP.loteVencido() ? "VENCIDO" : String.valueOf(loteP.diasAteVencer());
            } else {
                linha[5] = "Não";
                linha[6] = "N/A"; // Não aplicável
            }
            
            // Adiciona a linha ao modelo da tabela
            tableModel.addRow(linha);
        }
    }
}