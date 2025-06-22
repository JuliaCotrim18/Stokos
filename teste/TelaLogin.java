// TelaLogin.java

import javax.swing.*;
import java.awt.*;

/**
 * a classe TelaLogin representa a primeira janela
 * que o usuário verá ao iniciar o programa.
 * Ele pede o nome do usuário e a senha.
 * Ela herda de JFrame, é uma janela
 */

 public class TelaLogin extends JFrame
 {
    private JPanel painel;
    private JLabel labelUsuario;
    private JTextField campoUsuario;
    private JLabel labelSenha;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;

    // Construtor da classe
    public TelaLogin()
    {
        super("Stokos - Login");
        this.setSize(350, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        painel = new JPanel();
        painel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        labelUsuario = new JLabel("Usuário:");
        campoUsuario = new JTextField(20);

        labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);

        botaoEntrar = new JButton("Entrar");

        painel.add(labelUsuario);
        painel.add(campoUsuario);
        painel.add(labelSenha);
        painel.add(campoSenha);
        painel.add(botaoEntrar);

        this.add(painel);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);
            }
        });
    }
 }