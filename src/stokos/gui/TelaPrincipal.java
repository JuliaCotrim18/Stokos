package stokos.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
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

    /**
     * Construtor da tela principal.
     * Orquestra a montagem da janela e seus componentes.
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
        this.setSize(400, 500);
        // Define que a aplicação deve ser encerrada ao fechar a tela principal.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centraliza a janela.
    }

    /**
     * Inicializa e monta os componentes da tela principal.
     */
    private void inicializarComponentes() {
        // Obtém o contexto da aplicação para acessar informações globais, como o usuário logado.
        AppContext app = AppContext.getInstance();
        Usuario usuarioLogado = app.getUsuarioLogado();

        // Cria o painel principal que abrigará os botões de menu.
        JPanel painelDeBotoes = new JPanel();
        // Usa BoxLayout no eixo Y para empilhar os componentes verticalmente.
        painelDeBotoes.setLayout(new BoxLayout(painelDeBotoes, BoxLayout.Y_AXIS));
        painelDeBotoes.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Cria e estiliza a mensagem de boas-vindas.
        JLabel labelBoasVindas = new JLabel("Bem-vindo(a), " + usuarioLogado.getNomeDeUsuario() + "!");
        labelBoasVindas.setFont(new Font("Arial", Font.BOLD, 16));
        labelBoasVindas.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o texto.

        // Cria os botões de menu usando um método auxiliar para manter a consistência.
        JButton botaoAvisos = criarBotaoMenu("Avisos e Alertas");
        JButton botaoRelatorios = criarBotaoMenu("Relatórios");
        JButton botaoProdutos = criarBotaoMenu("Gerenciar Produtos (Catálogo)");
        JButton botaoEstoque = criarBotaoMenu("Gerenciar Estoque (Lotes)");
        JButton botaoSair = criarBotaoMenu("Sair (Logout)");

        // --- Adiciona os Action Listeners para cada botão ---
        // Cada listener define a ação de navegar para a tela correspondente.

        botaoEstoque.addActionListener(e -> {
            new TelaEstoque().setVisible(true); // Abre a tela de estoque.
            this.dispose(); // Fecha a tela atual.
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

        // O botão "Sair" tem uma lógica adicional: limpar o estado da sessão.
        botaoSair.addActionListener(e -> {
            app.setUsuarioLogado(null); // Remove a referência ao usuário logado do AppContext.
            new TelaLogin().setVisible(true); // Retorna para a tela de login.
            this.dispose();
        });

        // Adiciona os componentes ao painel, usando espaçadores para um layout agradável.
        painelDeBotoes.add(labelBoasVindas);
        painelDeBotoes.add(Box.createVerticalGlue()); // Espaço flexível para empurrar os botões.
        painelDeBotoes.add(botaoAvisos);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço fixo.
        painelDeBotoes.add(botaoRelatorios);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoProdutos);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoEstoque);
        painelDeBotoes.add(Box.createVerticalGlue()); // Espaço flexível.
        painelDeBotoes.add(botaoSair);

        botaoSair.setBackground(new Color(220, 53, 69)); // Um tom de vermelho escuro (R, G, B)
        botaoSair.setForeground(Color.WHITE); // Texto branco para melhor contraste

        // Adiciona o painel de botões a um JScrollPane para o caso de a janela ser pequena.
        JScrollPane scrollPane = new JScrollPane(painelDeBotoes);
        this.add(scrollPane);
    }

    /**
     * Método auxiliar (helper method) para criar e estilizar os botões do menu.
     * Usar um método auxiliar para isso promove a reutilização de código e garante
     * que todos os botões de menu tenham uma aparência consistente.
     *
     * @param texto O texto a ser exibido no botão.
     * @return um JButton configurado e pronto para ser adicionado à tela.
     */
    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        // Garante que o botão ocupe toda a largura disponível.
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza no eixo X do BoxLayout.
        return botao;
    }
}