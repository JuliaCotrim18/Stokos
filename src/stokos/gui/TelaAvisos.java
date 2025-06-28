package stokos.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import stokos.AppContext;
import stokos.model.CatalogoDeProdutos;
import stokos.model.Lote;
import stokos.model.Produto;
import stokos.model.LotePerecivel;
import stokos.model.Estoque;

/**
 * Representa a tela de Avisos e Alertas do sistema.
 * Esta tela funciona como um "dashboard", exibindo informações críticas que
 * exigem a atenção do usuário, como lotes vencidos ou próximos do vencimento,
 * e produtos com estoque baixo.
 */
public class TelaAvisos extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JList<String> listaAvisos; // Componente para exibir a lista de alertas.
    private DefaultListModel<String> listModel; // O modelo de dados que alimenta a JList.

    /**
     * Construtor da tela de Avisos.
     * Orquestra a configuração da janela e o carregamento dos dados.
     */
    public TelaAvisos() {
        super("Stokos - Avisos e Alertas");
        configurarJanela();
        inicializarComponentes();
        // Carrega e exibe os avisos reais do sistema.
        carregarAvisos();
    }

    /**
     * Configura as propriedades principais da janela (JFrame).
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
            new TelaPrincipal().setVisible(true); // Retorna à tela principal.
            this.dispose(); // Libera os recursos da tela atual.
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel central que contém a lista de avisos.
     * @return O JPanel configurado.
     */
    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createTitledBorder("Painel de Avisos"));

        // O DefaultListModel é o "cérebro" da JList. Adicionamos os avisos a ele.
        listModel = new DefaultListModel<>();
        listaAvisos = new JList<>(listModel);
        listaAvisos.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fonte monoespaçada para alinhamento.

        // Adiciona a lista a um JScrollPane para permitir rolagem se houver muitos avisos.
        JScrollPane scrollPane = new JScrollPane(listaAvisos);
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        return painelCentral;
    }

    /**
     * Carrega os avisos do sistema, processando a lógica de negócio para
     * identificar alertas de estoque e validade.
     */
    private void carregarAvisos() {
        listModel.clear(); // Limpa a lista para garantir que os avisos não se acumulem.

        // Acessa os dados do sistema através do AppContext.
        AppContext app = AppContext.getInstance();
        Estoque estoque = app.getDados().estoque;
        CatalogoDeProdutos catalogo = app.getDados().catalogo;

        // Listas auxiliares para evitar alertas duplicados para o mesmo produto.
        ArrayList<String> produtosComAvisoVencido = new ArrayList<>();
        ArrayList<String> produtosComAvisoProximo = new ArrayList<>();

        // 1. VERIFICAÇÃO DE VALIDADE DOS LOTES
        for (Lote lote : estoque.getLotes()) {
            Produto produtoDoLote = lote.getProduto();
            String nomeProduto = produtoDoLote.getNomeDoProduto();

            // POLIMORFISMO: O método `loteVencido()` é chamado. O Java executa a
            // implementação correta dependendo se o lote é Perecivel ou NaoPerecivel.
            if (lote.loteVencido()) {
                if (!produtosComAvisoVencido.contains(nomeProduto)) {
                    listModel.addElement("ALERTA: Há um ou mais lotes do produto '" + nomeProduto + "' vencidos no estoque.");
                    produtosComAvisoVencido.add(nomeProduto); // Marca o produto para não repetir o aviso.
                }
            } else if (lote.estaPertoDeVencer()) { // Outro exemplo de polimorfismo.
                if (!produtosComAvisoProximo.contains(nomeProduto)) {
                    // Casting para LotePerecivel é necessário para acessar o método diasAteVencer().
                    int dias = ((LotePerecivel) lote).diasAteVencer();
                    listModel.addElement("AVISO: O produto '" + nomeProduto + "' tem um lote que vence em " + dias + " dia(s).");
                    produtosComAvisoProximo.add(nomeProduto);
                }
            }
        }

        // 2. VERIFICAÇÃO DE ESTOQUE MÍNIMO E ZERADO
        for (Produto produto : catalogo.getListaDeProdutos()) {
            double estoqueMinimo = produto.getEstoqueMinimo();
            double quantidadeAtual = estoque.getQuantidadeDisponivel(produto.getCodigoDeBarras());

            // Verifica primeiro a condição mais crítica: estoque zerado.
            if (quantidadeAtual == 0) {
                listModel.addElement("ESTOQUE ZERADO: O produto '" + produto.getNomeDoProduto() + "' acabou.");
            
            // Se não estiver zerado, verifica se está abaixo do mínimo configurado.
            } else if (estoqueMinimo > 0 && quantidadeAtual <= estoqueMinimo) {
                listModel.addElement("ESTOQUE: O produto '" + produto.getNomeDoProduto() + "' está com estoque baixo (" + quantidadeAtual + " / " + estoqueMinimo + " " + produto.getGrandeza().toString().toLowerCase() + ").");
            }
        }

        // 3. MENSAGEM PADRÃO SE NÃO HOUVER AVISOS
        // Se, após todas as verificações, a lista continuar vazia, exibe uma mensagem informativa.
        if (listModel.isEmpty()) {
            listModel.addElement("Nenhum aviso ou alerta no momento.");
        }
    }
}