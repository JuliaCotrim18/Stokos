// TelaPrincipal.java

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame
{
    private JButton botaoAvisos;
    private JButton botaoRelatorios;
    private JButton botaoProdutos;
    private JButton botaoEstoque;

    public TelaPrincipal()
    {
        super("Stokos - Menu Principal");
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel painelDeBotoes = new JPanel();

        painelDeBotoes.setLayout(new BoxLayout(painelDeBotoes, BoxLayout.Y_AXIS));
        painelDeBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cria os botões do menu
        botaoAvisos = new JButton("Avisos e Alertas");
        botaoRelatorios = new JButton("Relatórios");
        botaoProdutos = new JButton("Produtos");
        botaoEstoque = new JButton("Estoque");

        // Adiciona os botões ao painel
        painelDeBotoes.add(botaoAvisos);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoRelatorios);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));      
        painelDeBotoes.add(botaoProdutos);
        painelDeBotoes.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDeBotoes.add(botaoEstoque);

        // Cria o painel com rolagem e coloca nosso painel de botões DENTRO

        JScrollPane scrollPane = new JScrollPane(painelDeBotoes);

        // Adiciona o painel de rolagem ao JFrame
        this.add(scrollPane);

    }

    // Main para teste
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}