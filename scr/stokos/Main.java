// Main.java (Refatorada com AppContext)
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da aplicação Stokos.
 * A única responsabilidade desta classe é iniciar o contexto da aplicação
 * e a interface gráfica inicial.
 */
public class Main {

    public static void main(String[] args) {

        // --- PASSO 1: Obter/Iniciar o contexto global da aplicação ---
        // Esta única linha é agora o coração da inicialização.
        // No momento em que ela é chamada pela primeira vez, o Java executa toda
        // a lógica que colocamos no construtor privado do AppContext: os serviços
        // são criados e o método carregarDados() é chamado automaticamente.
        final AppContext app = AppContext.getInstance();


        // --- PASSO 2: Configurar o Salvamento Automático ---
        // A lógica de salvamento agora é delegada para o AppContext.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Desligando o sistema...");
            app.salvarDados();
            System.out.println("Dados salvos. Até logo!");
        }));


        // --- PASSO 3: Iniciar a Interface Gráfica ---
        // Inicia a GUI na thread de eventos do Swing para segurança.
        SwingUtilities.invokeLater(() -> {
            // A TelaLogin se tornou independente. Ela mesma pegará o que
            // precisa do AppContext quando for necessário.
            // Por isso, seu construtor agora é chamado sem argumentos.
            new TelaLogin().setVisible(true);
        });
    }
}