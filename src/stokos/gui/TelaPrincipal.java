// TelaPrincipal.java (versão com AppContext)

package stokos.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import stokos.model.*;
import stokos.AppContext;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        super("Stokos - Menu Principal");
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // Pega a instância do AppContext para obter informações
        AppContext app = AppContext.getInstance();
        Usuario usuarioLogado = app.getUsuarioLogado();

        // Painel principal com os botões
        JPanel painelDeBotoes = new JPanel();
        painelDeBotoes.setLayout(new BoxLayout(painelDeBotoes, BoxLayout.Y_AXIS));
        painelDeBotoes.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Boas-vindas ao usuário ---
        JLabel labelBoasVindas = new JLabel("Bem-vindo(a), " + usuarioLogado.getNomeDeUsuario() + "!");
        labelBoasVindas.setFont(new Font("Arial", Font.BOLD, 16));
        labelBoasVindas.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Botões do Menu ---
        JButton botaoAvisos = criarBotaoMenu("Avisos e Alertas");
        JButton botaoRelatorios = criarBotaoMenu("Relatórios");
        JButton botaoProdutos = criarBotaoMenu("Gerenciar Produtos (Catálogo)");
        JButton botaoEstoque = criarBotaoMenu("Gerenciar Estoque (Lotes)");
        JButton botaoSair = criarBotaoMenu("Sair (Logout)");

       // Adiciona a ação para o botão de Estoque
       botaoEstoque.addActionListener(e -> 
       {
            new TelaEstoque().setVisible(true);
            this.dispose();
       }); 

        // Adiciona a ação para o botão de Produtos
        botaoProdutos.addActionListener(e -> {
            new TelaProdutos().setVisible(true);
            this.dispose();
        });
        
        // Adiciona a ação para o botão de Sair
        botaoSair.addActionListener(e -> {
            app.setUsuarioLogado(null); // Limpa o usuário da sessão
            new TelaLogin().setVisible(true);
            this.dispose();
        });

        // Adiciona a ação para o botao de avisos
        botaoAvisos.addActionListener(e -> 
        {
            new TelaAvisos().setVisible(true);
            this.dispose();

        });

        // Adiciona a ação para o botão de Relatórios
        botaoRelatorios.addActionListener(e -> 
        {
            new TelaRelatorio().setVisible(true);
            this.dispose();
        }); 
        

        // Adiciona tudo ao painel
        painelDeBotoes.add(labelBoasVindas);
        painelDeBotoes.add(Box.createVerticalGlue()); // Espaço flexível
        painelDeBotoes.add(botaoAvisos);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoRelatorios);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoProdutos);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoEstoque);
        painelDeBotoes.add(Box.createVerticalGlue()); // Espaço flexível
        painelDeBotoes.add(botaoSair);
        
        JScrollPane scrollPane = new JScrollPane(painelDeBotoes);
        this.add(scrollPane);
    }

    // Método ajudante para criar botões padronizados
    private JButton criarBotaoMenu(String texto) {
        JButton botao = new JButton(texto);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Altura fixa, largura máxima
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        return botao;
    }
}