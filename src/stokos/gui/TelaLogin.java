package stokos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import stokos.model.*;
import stokos.AppContext;


public class TelaLogin extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;

    public TelaLogin() {
        super("Stokos - Login");
        configurarJanela();
        inicializarComponentes();
        adicionarListeners();
    }
    
    private void configurarJanela() {
        this.setSize(350, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(3, 2, 10, 10));
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void inicializarComponentes() {
        this.add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        this.add(campoUsuario);
        this.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        this.add(campoSenha);
        this.add(new JLabel()); 
        botaoEntrar = new JButton("Entrar");
        this.add(botaoEntrar);
    }
    
    private void adicionarListeners() {
        botaoEntrar.addActionListener(e -> realizarLogin());
    }
    
    private void realizarLogin() {
        AppContext app = AppContext.getInstance();
        String nomeUsuario = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());

        Usuario usuarioAutenticado = app.getServicoDeAutenticacao().autenticar(nomeUsuario, senha);

        if (usuarioAutenticado != null) {
            app.setUsuarioLogado(usuarioAutenticado);
            JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioAutenticado.getNomeDeUsuario() + "!");
            
            TelaPrincipal telaPrincipal = new TelaPrincipal();
            telaPrincipal.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}