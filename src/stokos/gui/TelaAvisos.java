package stokos.gui;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList; // Importar ArrayList
import java.util.HashMap; // Importar HashMap
import java.util.Map; // Importar Map


import stokos.AppContext;
import stokos.model.CatalogoDeProdutos;
import stokos.model.Lote;
import stokos.model.Produto;

public class TelaAvisos extends JFrame 
{

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JList<String> listaAvisos;
    private DefaultListModel<String> listModel;

    // --- Construtor ---
    public TelaAvisos() {
        super("Stokos - Avisos e Alertas");

        configurarJanela();
        inicializarComponentes();
        //carregarAvisosExemplo(); // Carrega os dados de exemplo
        carregarAvisos();

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

    private void carregarAvisos()
    {
         listModel.clear(); // Limpa a lista antes de adicionar novos avisos

        AppContext app = AppContext.getInstance();
        ArrayList<Lote> todosOsLotes = app.getDados().estoque.getLotes();
        CatalogoDeProdutos catalogo = app.getDados().catalogo;

        // Listas para controlar quais produtos já tiveram seu aviso gerado
        ArrayList<String> produtosComAvisoVencido = new ArrayList<>();
        ArrayList<String> produtosComAvisoProximo = new ArrayList<>();

        for (Lote lote : todosOsLotes) 
        {
            Produto produto = lote.getProduto();
            String nomeProduto = produto.getNomeDoProduto();

            // 1. Verifica lotes vencidos
            if (lote.loteVencido()) {
                // Se ainda não adicionamos um aviso para este produto, adicione agora
                if (!produtosComAvisoVencido.contains(nomeProduto)) {
                    listModel.addElement("ALERTA: Há um ou mais lotes do produto '" + nomeProduto + "' vencidos no estoque.");
                    produtosComAvisoVencido.add(nomeProduto); // Marca o produto como processado
                }
            
            // 2. Verifica lotes próximos do vencimento
            } else if (lote.estaPertoDeVencer()) {
                // Se ainda não adicionamos um aviso para este produto, adicione agora
                // A mensagem pegará os dados do primeiro lote "próximo de vencer" que encontrar
                if (!produtosComAvisoProximo.contains(nomeProduto)) {
                    int dias = ((LotePerecivel) lote).diasAteVencer();
                    listModel.addElement("AVISO: O produto '" + nomeProduto + "' tem um lote que vence em " + dias + " dia(s).");
                    produtosComAvisoProximo.add(nomeProduto); // Marca o produto como processado
                }
            }
        }

        // lógica de aviso de estoque baixo
        for (Produto produto : catalogo.getListaDeProdutos())
        {
             double estoqueMinimo = produto.getEstoqueMinimo();
            // Só gera aviso se o estoque mínimo for maior que zero
            if (estoqueMinimo > 0) {
                int quantidadeAtual = estoque.getQuantidadeDisponivel(produto.getCodigoDeBarras());
                if (quantidadeAtual <= estoqueMinimo) {
                    listModel.addElement("ESTOQUE: O produto '" + produto.getNomeDoProduto() + "' está com estoque baixo (" + quantidadeAtual + " / " + estoqueMinimo + " " + produto.getGrandeza().toString().toLowerCase() + ").");
                }
            }

        }
        
        // 3. Se não houver nenhum aviso, exibe uma mensagem padrão
        if (listModel.isEmpty()) 
        {
            listModel.addElement("Nenhum aviso ou alerta no momento.");
        }
    
    }
        


}
