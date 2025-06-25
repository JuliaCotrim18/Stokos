package stokos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// ... (outras importações)
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import stokos.AppContext;
import stokos.model.*;
import stokos.exception.ProdutoNaoCadastradoException;

public class TelaAdicionarLote extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoQuantidade;
    private JTextField campoFornecedor;
    private JTextField campoCusto;
    private JRadioButton radioPerecivel;
    private JLabel labelDataValidade; // Rótulo para o campo que aparece/desaparece
    private JTextField campoDataValidade; // Campo que aparece/desaparece
    private JButton botaoAdicionar;

    // --- Construtor ---
    public TelaAdicionarLote() {
        super("Adicionar Novo Lote ao Estoque");

        configurarJanela();
        inicializarComponentes();
        adicionarListeners(); // Adiciona a lógica de interação
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(550, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelFormularioScrollable(), BorderLayout.CENTER);
        this.add(criarPainelSul(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> 
        {
            new TelaEstoque().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    private JScrollPane criarPainelFormularioScrollable() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Código de Barras
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(new JLabel("Código de Barras do Produto:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campoCodigoBarras = new JTextField(20);
        painelFormulario.add(campoCodigoBarras, gbc);

        // Linha 1: Quantidade
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        campoQuantidade = new JTextField(20);
        painelFormulario.add(campoQuantidade, gbc);

        // Linha 2: Fornecedor
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelFormulario.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1;
        campoFornecedor = new JTextField(20);
        painelFormulario.add(campoFornecedor, gbc);

        // Linha 3: Custo
        gbc.gridx = 0;
        gbc.gridy = 3;
        painelFormulario.add(new JLabel("Custo (Total do Lote):"), gbc);
        gbc.gridx = 1;
        campoCusto = new JTextField(20);
        painelFormulario.add(campoCusto, gbc);

        // Linha 4: Lote Perecível (RadioButton)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupa duas colunas
        radioPerecivel = new JRadioButton("Este lote é perecível?");
        painelFormulario.add(radioPerecivel, gbc);

        // Linha 5: Data de Validade (inicialmente invisível)
        gbc.gridy = 5;
        gbc.gridwidth = 1; // Volta a ocupar uma coluna
        gbc.gridx = 0;
        labelDataValidade = new JLabel("Data de Validade (dd/mm/aaaa):");
        painelFormulario.add(labelDataValidade, gbc);

        gbc.gridx = 1;
        campoDataValidade = new JTextField(20);
        painelFormulario.add(campoDataValidade, gbc);

        // Esconde os campos de data de validade por padrão
        labelDataValidade.setVisible(false);
        campoDataValidade.setVisible(false);

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados do Lote"));
        return scrollPane;
    }

    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoAdicionar = new JButton("Adicionar Lote");
        botaoAdicionar.setPreferredSize(new Dimension(150, 30));
        botaoAdicionar.addActionListener(e -> 
        {
            try {
            // 1. Obter dados da interface
            String codigoBarras = campoCodigoBarras.getText().trim();
            int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

            // 2. Validação inicial
            if (codigoBarras.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Código de Barras' é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Acessar o contexto e buscar o produto
            AppContext app = AppContext.getInstance();
            CatalogoDeProdutos catalogo = app.getDados().catalogo;
            Produto produtoDoLote = catalogo.buscarProduto(codigoBarras);

            if (produtoDoLote == null) {
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado com o código de barras informado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Criar o lote (usando polimorfismo)
            Lote novoLote;
            if (radioPerecivel.isSelected()) {
                // Lote Perecível
                String textoData = campoDataValidade.getText().trim();
                if (textoData.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "A data de validade é obrigatória para lotes perecíveis.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataValidade = LocalDate.parse(textoData, formatador);
                novoLote = new LotePerecivel(produtoDoLote, quantidade, dataValidade);
            } else {
                // Lote Não Perecível
                novoLote = new LoteNaoPerecivel(produtoDoLote, quantidade);
            }

            // 5. Adicionar lote ao estoque
            app.getDados().estoque.adicionarLote(novoLote);
            JOptionPane.showMessageDialog(this, "Lote adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // 6. Voltar para a tela de estoque
            new TelaEstoque().setVisible(true);
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade e o custo devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/mm/aaaa.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (ProdutoNaoCadastradoException ex) {
            // Esta exceção é lançada pelo estoque.adicionarLote
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        });
        painelSul.add(botaoAdicionar);
        return painelSul;
    }

    /**
     * Adiciona os 'listeners' para os componentes interativos da janela.
     */
    private void adicionarListeners() {
        radioPerecivel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se o radio button está selecionado
                boolean selecionado = radioPerecivel.isSelected();

                // Altera a visibilidade dos componentes de data de validade
                labelDataValidade.setVisible(selecionado);
                campoDataValidade.setVisible(selecionado);
            }
        });
    }
}