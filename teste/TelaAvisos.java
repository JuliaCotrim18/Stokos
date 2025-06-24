import javax.swing.*;
import java.awt.*;

public class TelaAvisos extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JList<String> listaAvisos;
    private DefaultListModel<String> listModel;

    // --- Construtor ---
    public TelaAvisos() {
        super("Stokos - Avisos e Alertas");

        configurarJanela();
        inicializarComponentes();
        carregarAvisosExemplo(); // Carrega os dados de exemplo
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(600, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelCentral(), BorderLayout.CENTER);
    }

    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> 
        {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    private JPanel criarPainelCentral() {
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBorder(BorderFactory.createTitledBorder("Painel de Avisos"));

        listModel = new DefaultListModel<>();
        listaAvisos = new JList<>(listModel);
        listaAvisos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        listaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaAvisos);
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        return painelCentral;
    }
    
    /**
     * Método de exemplo para popular a lista com os dados que você sugeriu.
     */
    private void carregarAvisosExemplo() {
        // --- TEXTO ATUALIZADO AQUI ---
        listModel.addElement("Produto 'Leite' com estoque baixo (apenas 5 unidades restantes).");
        listModel.addElement("Lote #123 de 'Iogurte' irá expirar em 3 dias.");
        listModel.addElement("ALERTA: Lote #100 de 'Carne Moída' está vencido.");
    }
}