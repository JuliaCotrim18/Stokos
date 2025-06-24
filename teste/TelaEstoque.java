import javax.swing.*;
import java.awt.*;

public class TelaEstoque extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JButton botaoAdicionarLote;
    private JButton botaoRegistrarSaida;

    // --- Construtor ---
    public TelaEstoque() {
        super("Stokos - Gerenciar Estoque"); // Título da janela

        configurarJanela();
        inicializarComponentes();
        // A lógica dos botões (Listeners) será adicionada depois.
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centraliza na tela
        this.setLayout(new BorderLayout(10, 10)); // Layout principal
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margem geral
    }

    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelCentral(), BorderLayout.CENTER);
    }

    /**
     * Cria o painel superior com o botão "Voltar" alinhado à esquerda.
     * @return JPanel configurado.
     */
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
     * Cria o painel central com os botões de ação verticalmente centralizados.
     * @return JPanel configurado.
     */
    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel();
        // Define o layout como uma "caixa" vertical
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

        // Cria os botões de ação
        botaoAdicionarLote = new JButton("Adicionar Novo Lote");

        botaoAdicionarLote.addActionListener(e -> 
        {
            new TelaAdicionarLote().setVisible(true);
            this.dispose();
        });


        botaoRegistrarSaida = new JButton("Registrar Saída de Produto");
        botaoRegistrarSaida.addActionListener(e -> 
        {
            new TelaRegistrarSaida().setVisible(true);
            this.dispose();
        });

        // Padroniza o alinhamento e o tamanho dos botões
        configurarBotao(botaoAdicionarLote);
        configurarBotao(botaoRegistrarSaida);

        // --- Mágica da Centralização ---
        painelCentral.add(Box.createVerticalGlue()); // Mola superior
        painelCentral.add(botaoAdicionarLote);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço de 20px entre os botões
        painelCentral.add(botaoRegistrarSaida);
        painelCentral.add(Box.createVerticalGlue()); // Mola inferior

        return painelCentral;
    }

    /**
     * Método auxiliar para aplicar um estilo padrão aos botões de ação.
     * @param botao O botão a ser configurado.
     */
    private void configurarBotao(JButton botao) {
        botao.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o botão no eixo X
        botao.setMaximumSize(new Dimension(250, 50));   // Define um tamanho máximo
        botao.setPreferredSize(new Dimension(250, 50)); // Define um tamanho preferido
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
    }
}