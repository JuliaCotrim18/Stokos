package stokos.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import stokos.AppContext;
import stokos.model.Lote;
import stokos.model.LotePerecivel;

/**
 * Representa a tela de visualização que exibe todos os lotes atualmente
 * presentes no estoque em um formato de tabela.
 */
public class TelaVisualizarEstoque extends JFrame {

    // --- Atributos da UI ---
    private JTable tabelaEstoque; // O componente visual que exibe a tabela.
    private DefaultTableModel tableModel; // O modelo que gerencia os dados da tabela (linhas e colunas).
    private JButton botaoVoltar;

    /**
     * Construtor da tela de visualização de estoque.
     */
    public TelaVisualizarEstoque() {
        super("Stokos - Visualização de Estoque");
        configurarJanela();
        inicializarComponentes();
        // Carrega os dados do estoque e popula a tabela assim que a tela é criada.
        carregarDadosDaTabela();
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
    }

    /**
     * Cria o painel superior (Norte) com o botão de navegação "Voltar".
     * @return O JPanel configurado.
     */
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

    /**
     * Cria o painel central que contém a tabela de visualização do estoque.
     * @return Um JScrollPane contendo a JTable.
     */
    private JScrollPane criarPainelCentral() {
        // 1. Define os nomes das colunas da tabela.
        String[] colunas = {
            "ID do Lote", "Produto", "Quantidade", "Fornecedor",
            "Custo Total", "Perecível?", "Dias até Vencer"
        };

        // 2. Cria o modelo da tabela. O '0' indica que ela começa sem nenhuma linha.
        // O TableModel é o "cérebro" da JTable, responsável por gerenciar os dados.
        tableModel = new DefaultTableModel(colunas, 0);

        // 3. Cria a JTable, associando-a ao modelo de dados criado.
        tabelaEstoque = new JTable(tableModel);

        // Configurações visuais para uma melhor experiência do usuário.
        tabelaEstoque.setRowHeight(25);
        tabelaEstoque.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaEstoque.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // 4. Adiciona a tabela a um JScrollPane para habilitar a barra de rolagem.
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lotes Atuais no Estoque"));

        return scrollPane;
    }

    /**
     * Busca os dados dos lotes no sistema e popula a JTable.
     */
    private void carregarDadosDaTabela() {
        // Acessa os dados do estoque através do AppContext.
        AppContext app = AppContext.getInstance();
        ArrayList<Lote> lotes = app.getDados().estoque.getLotes();

        // Ordena a lista de lotes pelo ID. Isso garante que a exibição
        // seja consistente e previsível toda vez que a tela é aberta.
        lotes.sort(Comparator.comparingInt(Lote::getId));

        // Itera sobre cada lote para criar uma linha correspondente na tabela.
        for (Lote lote : lotes) {
            // Cria um array de Object para representar os dados de uma única linha.
            Object[] linha = new Object[7];

            linha[0] = lote.getId();
            linha[1] = lote.getProduto().getNomeDoProduto();
            linha[2] = lote.getQuantidade();
            linha[3] = lote.getFornecedor();
            linha[4] = lote.getCustoDoLote();

            // LÓGICA POLIMÓRFICA PARA EXIBIÇÃO
            // Verifica se o lote é uma instância de LotePerecivel para tratar
            // as colunas de dados de validade de forma diferente.
            if (lote instanceof LotePerecivel) {
                // Se for perecível, faz o "cast" para LotePerecivel para acessar seus métodos específicos.
                LotePerecivel loteP = (LotePerecivel) lote;
                linha[5] = "Sim";
                // Usa um operador ternário para exibir "VENCIDO" ou o número de dias restantes.
                linha[6] = loteP.loteVencido() ? "VENCIDO" : String.valueOf(loteP.diasAteVencer());
            } else {
                // Se for um LoteNaoPerecivel, preenche as colunas com informações padrão.
                linha[5] = "Não";
                linha[6] = "N/A"; // "Não Aplicável"
            }

            // Adiciona a linha preenchida ao modelo da tabela, o que a torna visível na UI.
            tableModel.addRow(linha);
        }
    }
}