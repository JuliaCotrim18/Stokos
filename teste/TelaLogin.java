// TelaLogin.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {

    // --- Atributos ---
    // Atributo para guardar a referência ao serviço de autenticação
    private ServicoDeAutenticacao servicoAuth;
    private DadosDoSistema dados;

    // Componentes da interface
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;

    // --- Construtor ---
    /**
     * Construtor da TelaLogin.
     * @param servicoAuth A instância do serviço de autenticação que a tela usará.
     */
    public TelaLogin(DadosDoSistema dados, ServicoDeAutenticacao servicoAuth) 
    {
        super("Stokos - Login");

        // 1. Injeta a dependência
        this.servicoAuth = servicoAuth;
        this.dados = dados;

        // 2. Configura a janela
        this.setSize(350, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(3, 2, 10, 10)); // Layout de grade para organizar

        // 3. Cria e adiciona os componentes
        this.add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        this.add(campoUsuario);

        this.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        this.add(campoSenha);

        // Adiciona um componente vazio para pular uma célula da grade
        this.add(new JLabel()); 

        botaoEntrar = new JButton("Entrar");
        this.add(botaoEntrar);

        // 4. Adiciona o Event Handling (Ação do Botão)
        adicionarListenerBotaoEntrar();
    }

    /**
     * Método privado que configura o ActionListener para o botão de login.
     */
    private void adicionarListenerBotaoEntrar() {
        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pega os dados digitados pelo usuário
                String nomeUsuario = campoUsuario.getText();
                String senha = new String(campoSenha.getPassword());

                // Usa o serviço de autenticação para validar
                Usuario usuarioAutenticado = servicoAuth.autenticar(nomeUsuario, senha);

                if (usuarioAutenticado != null) {
                    // Se o login for bem-sucedido...
                    JOptionPane.showMessageDialog(TelaLogin.this, "Bem-vindo(a), " + usuarioAutenticado.getNomeDeUsuario() + "!");
                    
                    // Abre a tela principal
                   
                    TelaPrincipal telaPrincipal = new TelaPrincipal(dados, usuarioAutenticado);
                    telaPrincipal.setVisible(true);

                    // Fecha a tela de login
                    TelaLogin.this.dispose();

                } else {
                    // Se o login falhar, mostra uma mensagem de erro
                    JOptionPane.showMessageDialog(TelaLogin.this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args)
    {
        // Cria uma instância de DadosDoSistema e ServicoDeAutenticacao
        DadosDoSistema dados = new DadosDoSistema();
        ServicoDeAutenticacao servicoAuth = new ServicoDeAutenticacao();

        // Cria e exibe a tela de login
        TelaLogin telaLogin = new TelaLogin(dados, servicoAuth);
        telaLogin.setVisible(true);
    }
}