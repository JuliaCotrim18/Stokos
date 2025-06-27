package stokos;

import javax.swing.SwingUtilities;
import stokos.gui.TelaLogin;

/**
 * Ponto de entrada da aplicação Stokos.
 * A responsabilidade desta classe é unicamente iniciar os processos essenciais:
 * o contexto da aplicação e a interface gráfica.
 */
public class Main {

    /**
     * Método principal que a JVM executa para iniciar o programa.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {

        // 1. OBTENÇÃO DO CONTEXTO DA APLICAÇÃO
        // Utiliza o padrão Singleton para obter a instância única do AppContext.
        // A primeira chamada a `getInstance()` carrega os dados e inicializa os serviços.
        final AppContext app = AppContext.getInstance();

        // 2. CONFIGURAÇÃO DO SALVAMENTO AUTOMÁTICO
        // Registra um "Shutdown Hook": uma thread que é executada automaticamente
        // antes de a aplicação fechar. Isto garante que os dados serão salvos.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Desligando o sistema... salvando dados.");
            app.salvarDados();
            System.out.println("Dados salvos. Até logo!");
        }));

        // 3. INICIALIZAÇÃO DA INTERFACE GRÁFICA (GUI)
        // `SwingUtilities.invokeLater` agenda a criação da GUI na Event Dispatch Thread (EDT).
        // Esta é a forma correta e segura de iniciar aplicações Swing, evitando problemas de concorrência.
        SwingUtilities.invokeLater(() -> {
            // Cria e torna visível a tela de login inicial.
            new TelaLogin().setVisible(true);
        });
    }
}