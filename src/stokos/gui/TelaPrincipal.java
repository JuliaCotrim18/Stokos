package stokos.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import stokos.model.Usuario;
import stokos.AppContext;

/**
 * Representa a tela do menu principal da aplicação, exibida após o login.
 * Esta tela serve como o principal ponto de navegação, a partir do qual o
 * usuário pode acessar as diferentes funcionalidades do sistema.
 */

public class TelaPrincipal extends JFrame {
    
    // Constantes para cores e fontes para fácil manutenção
    private static final Color COR_FUNDO = new Color(245, 245, 245);
    private static final Color COR_BOTAO_SAIR = new Color(220, 53, 69);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Construtor da tela principal.
     */
    public TelaPrincipal() {
        super("Stokos - Menu Principal");
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades essenciais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centraliza a janela.
        this.getContentPane().setBackground(COR_FUNDO); // Define uma cor de fundo suave
        this.setLayout(new BorderLayout());
    }

    /**
     * Inicializa e monta os componentes da tela principal.
     */
    private void inicializarComponentes() {
        AppContext app = AppContext.getInstance();
        Usuario usuarioLogado = app.getUsuarioLogado();

        // --- PAINEL NORTE: TÍTULO E SUBTÍTULO ---
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setOpaque(false); // Torna o painel transparente para mostrar a cor do frame
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelBoasVindas = new JLabel("Bem-vindo(a), " + usuarioLogado.getNomeDeUsuario() + "!");
        labelBoasVindas.setFont(FONTE_TITULO);
        labelBoasVindas.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelSubtitulo = new JLabel("Selecione uma opção para começar");
        labelSubtitulo.setFont(FONTE_BOTAO.deriveFont(Font.ITALIC));
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        painelTitulo.add(labelBoasVindas);
        painelTitulo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelTitulo.add(labelSubtitulo);

        // --- PAINEL CENTRAL: BOTÕES DE MENU ---
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        painelBotoes.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0); // Espaçamento vertical entre os botões

        JButton botaoAvisos = criarBotaoMenu("Avisos e Alertas");
        JButton botaoRelatorios = criarBotaoMenu("Relatórios");
        JButton botaoProdutos = criarBotaoMenu("Gerenciar Produtos (Catálogo)");
        JButton botaoEstoque = criarBotaoMenu("Gerenciar Estoque (Lotes)");
        
        painelBotoes.add(botaoAvisos, gbc);
        painelBotoes.add(botaoRelatorios, gbc);
        painelBotoes.add(botaoProdutos, gbc);
        painelBotoes.add(botaoEstoque, gbc);

        // --- PAINEL SUL: BOTÃO DE SAIR ---
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelSair.setOpaque(false);
        painelSair.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton botaoSair = criarBotaoMenu("Sair (Logout)");
        botaoSair.setBackground(COR_BOTAO_SAIR);
        botaoSair.setForeground(Color.WHITE);
        painelSair.add(botaoSair);

        // --- Adicionar Action Listeners ---
        botaoEstoque.addActionListener(e -> {
            new TelaEstoque().setVisible(true);
            this.dispose();
        });
        botaoProdutos.addActionListener(e -> {
            new TelaProdutos().setVisible(true);
            this.dispose();
        });
        botaoAvisos.addActionListener(e -> {
            new TelaAvisos().setVisible(true);
            this.dispose();
        });
        botaoRelatorios.addActionListener(e -> {
            new TelaRelatorios().setVisible(true);
            this.dispose();
        });
        botaoSair.addActionListener(e -> {
            app.setUsuarioLogado(null);
            new TelaLogin().setVisible(true);
            this.dispose();
        });
        
        // Adiciona os painéis principais à janela
        this.add(painelTitulo, BorderLayout.NORTH);
        this.add(painelBotoes, BorderLayout.CENTER);
        this.add(painelSair, BorderLayout.SOUTH);
    }

    /**
     * Método auxiliar para criar e estilizar os botões do menu.
     */
    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(FONTE_BOTAO);
        botao.setPreferredSize(new Dimension(250, 45)); // Tamanho padrão para os botões
        botao.setFocusPainted(false); // Remove a borda de foco ao clicar
        return botao;
    }
}