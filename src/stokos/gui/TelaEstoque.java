package stokos.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Representa a tela de menu para as funcionalidades de gerenciamento de estoque.
 * Esta tela atua como um ponto central de navegação, permitindo que o usuário
 * escolha entre adicionar um novo lote, registrar uma saída de produto ou
 * visualizar os lotes existentes.
 *
 * CONCEITO DE DESIGN: SEPARAÇÃO DA CONSTRUÇÃO DA UI
 * A classe é organizada em métodos privados, cada um responsável por criar uma
 * parte específica da interface (ex: `criarPainelNorte`, `criarPainelCentral`).
 * Essa abordagem torna o código mais limpo, legível e fácil de manter, em vez
 * de ter toda a lógica de construção da UI em um único método ou construtor.
 */
public class TelaEstoque extends JFrame {

    // --- Atributos de Componentes da UI ---
    // Atributos de classe para os componentes que precisam ser acessados em diferentes métodos.
    private JButton botaoVoltar;
    private JButton botaoAdicionarLote;
    private JButton botaoRegistrarSaida;
    private JButton botaoVisualizarEstoque;

    /**
     * Construtor da tela de gerenciamento de estoque.
     * Orquestra a chamada dos métodos de configuração e inicialização da janela.
     */
    public TelaEstoque() {
        super("Stokos - Gerenciar Estoque"); // Define o título da janela.
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades principais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(500, 400); // Define o tamanho da janela.
        // Define que a aplicação não deve fechar ao clicar no "X", apenas esta janela.
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centraliza a janela na tela.
        this.setLayout(new BorderLayout(10, 10)); // Define o layout principal.
        // Adiciona uma margem interna para um visual mais agradável.
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /**
     * Inicializa e organiza os painéis principais dentro da janela.
     */
    private void inicializarComponentes() {
        // Adiciona o painel com o botão "Voltar" na parte superior da janela.
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        // Adiciona o painel com os botões de ação na parte central.
        this.add(criarPainelCentral(), BorderLayout.CENTER);
    }

    /**
     * Cria o painel superior (Norte) contendo o botão "Voltar".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelNorte() {
        // Usa FlowLayout alinhado à esquerda para o botão "Voltar".
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");

        // Define a ação a ser executada quando o botão "Voltar" é clicado.
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true); // Abre a tela principal.
            this.dispose(); // Fecha a tela atual para liberar recursos.
        });

        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel central com os botões de navegação para as funcionalidades de estoque.
     * @return O JPanel configurado.
     */
    private JPanel criarPainelCentral() {
        // Usa BoxLayout no eixo Y para empilhar os botões verticalmente.
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

        // Instancia os botões de ação.
        botaoAdicionarLote = new JButton("Adicionar Novo Lote");
        botaoRegistrarSaida = new JButton("Registrar Saída de Produto");
        botaoVisualizarEstoque = new JButton("Visualizar Lotes no Estoque");

        // Associa as ações de clique a cada botão.
        // Cada ação consiste em abrir a tela correspondente e fechar a atual.
        botaoAdicionarLote.addActionListener(e -> {
            new TelaAdicionarLote().setVisible(true);
            this.dispose();
        });

        botaoRegistrarSaida.addActionListener(e -> {
            new TelaRegistrarSaida().setVisible(true);
            this.dispose();
        });

        botaoVisualizarEstoque.addActionListener(e -> {
            new TelaVisualizarEstoque().setVisible(true);
            this.dispose();
        });

        // Aplica uma formatação padrão aos botões para manter a consistência visual.
        configurarBotao(botaoAdicionarLote);
        configurarBotao(botaoRegistrarSaida);
        configurarBotao(botaoVisualizarEstoque);

        // Adiciona os componentes ao painel, usando espaçamento para um layout limpo.
        painelCentral.add(Box.createVerticalGlue()); // Espaço flexível para centralizar verticalmente.
        painelCentral.add(botaoAdicionarLote);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço fixo entre os botões.
        painelCentral.add(botaoRegistrarSaida);
        painelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        painelCentral.add(botaoVisualizarEstoque);
        painelCentral.add(Box.createVerticalGlue()); // Espaço flexível.

        return painelCentral;
    }

    /**
     * Método auxiliar para aplicar um estilo padrão e consistente aos botões de menu.
     * @param botao O botão a ser configurado.
     */
    private void configurarBotao(JButton botao) {
        botao.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza horizontalmente no BoxLayout.
        botao.setMaximumSize(new Dimension(250, 50));   // Define um tamanho máximo.
        botao.setPreferredSize(new Dimension(250, 50)); // Define um tamanho preferido.
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
    }
}