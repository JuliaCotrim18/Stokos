package stokos.gui;

import javax.swing.*;
import java.awt.*;
import stokos.model.Usuario;
import stokos.AppContext;

/**
 * Representa a tela de login da aplicação, a primeira interface com a qual
 * o usuário interage. Sua responsabilidade é coletar as credenciais do usuário
 * e solicitar a autenticação ao serviço correspondente.
 *
 * CONCEITO DE DESIGN: SEPARAÇÃO DE RESPONSABILIDADES (VIEW vs CONTROLLER)
 * A classe é responsável pela parte "View" (exibir os campos e o botão) e pela
 * parte "Controller" (lidar com o clique do botão). A lógica de negócio real

 * (como a senha está correta) é delegada para o `ServicoDeAutenticacao`,
 * mantendo a classe da GUI focada em suas tarefas.
 */
public class TelaLogin extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JTextField campoUsuario;
    private JPasswordField campoSenha; // JPasswordField é usado para que a senha não seja exibida.
    private JButton botaoEntrar;

    /**
     * Construtor da tela de login.
     * Chama os métodos responsáveis por montar e configurar a janela.
     */
    public TelaLogin() {
        super("Stokos - Login");
        configurarJanela();
        inicializarComponentes();
        adicionarListeners();
    }

    /**
     * Configura as propriedades visuais e de comportamento da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(350, 200);
        // Define que a aplicação deve ser encerrada ao fechar esta janela.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centraliza a janela na tela.
        // Usa GridLayout para organizar os componentes em uma grade simples.
        this.setLayout(new GridLayout(3, 2, 10, 10));
        // Adiciona uma margem interna.
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Cria e adiciona os componentes visuais (JLabels, JTextFields, etc.) à janela.
     */
    private void inicializarComponentes() {
        this.add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        this.add(campoUsuario);

        this.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        this.add(campoSenha);

        this.add(new JLabel()); // Célula vazia para alinhar o botão à direita.
        botaoEntrar = new JButton("Entrar");
        this.add(botaoEntrar);
    }

    /**
     * Associa os eventos (neste caso, o clique do botão) aos seus respectivos
     * métodos de tratamento (listeners).
     */
    private void adicionarListeners() {
        // Define que o método `realizarLogin()` deve ser executado quando o botão "Entrar" for clicado.
        botaoEntrar.addActionListener(e -> realizarLogin());
    }

    /**
     * Contém a lógica que é executada quando o usuário tenta fazer login.
     */
    private void realizarLogin() {
        // 1. Obtém a instância do AppContext para acessar os serviços.
        AppContext app = AppContext.getInstance();

        // 2. Coleta os dados dos campos da interface.
        String nomeUsuario = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword()); // `getPassword()` retorna char[], deve ser convertido para String.

        // 3. Delega a lógica de autenticação para o serviço apropriado.
        // A tela não sabe como a autenticação funciona, apenas chama o serviço.
        Usuario usuarioAutenticado = app.getServicoDeAutenticacao().autenticar(nomeUsuario, senha);

        // 4. Trata o resultado da autenticação.
        if (usuarioAutenticado != null) {
            // SUCESSO:
            // Define o usuário logado no contexto da aplicação para uso futuro.
            app.setUsuarioLogado(usuarioAutenticado);
            // Exibe uma mensagem de boas-vindas.
            JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioAutenticado.getNomeDeUsuario() + "!");

            // Navega para a próxima tela.
            new TelaPrincipal().setVisible(true);
            this.dispose(); // Fecha a tela de login.

        } else {
            // FALHA:
            // Exibe uma mensagem de erro para o usuário.
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}