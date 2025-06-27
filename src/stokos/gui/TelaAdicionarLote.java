package stokos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import stokos.AppContext;
import stokos.model.*;
import stokos.exception.ProdutoNaoCadastradoException;

/**
 * Representa a janela para adicionar um novo lote de um produto ao estoque.
 * A tela coleta os dados do lote e, de forma polimórfica, cria o tipo
 * correto de objeto Lote (`LotePerecivel` ou `LoteNaoPerecivel`).
 */
public class TelaAdicionarLote extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras, campoQuantidade, campoFornecedor, campoCusto;
    private JRadioButton radioPerecivel;
    private JLabel labelDataValidade;
    private JTextField campoDataValidade;
    private JButton botaoAdicionar;

    /**
     * Construtor da tela de adição de lote.
     */
    public TelaAdicionarLote() {
        super("Adicionar Novo Lote ao Estoque");
        configurarJanela();
        inicializarComponentes();
        adicionarListeners();
    }

    /**
     * Configura as propriedades essenciais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(550, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
    }

    /**
     * Inicializa e organiza os painéis principais dentro da janela.
     */
    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelFormularioScrollable(), BorderLayout.CENTER);
        this.add(criarPainelSul(), BorderLayout.SOUTH);
    }

    /**
     * Cria o painel superior (Norte) com o botão de navegação "Voltar".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            new TelaEstoque().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel central com o formulário de cadastro de lote.
     * @return Um JScrollPane contendo o painel do formulário.
     */
    private JScrollPane criarPainelFormularioScrollable() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Adiciona os campos do formulário à grade.
        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("Código de Barras do Produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoCodigoBarras = new JTextField(20); painelFormulario.add(campoCodigoBarras, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; painelFormulario.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; campoQuantidade = new JTextField(20); painelFormulario.add(campoQuantidade, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelFormulario.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1; campoFornecedor = new JTextField(20); painelFormulario.add(campoFornecedor, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelFormulario.add(new JLabel("Custo (Total do Lote):"), gbc);
        gbc.gridx = 1; campoCusto = new JTextField(20); painelFormulario.add(campoCusto, gbc);

        // Componentes para a lógica condicional de lote perecível.
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        radioPerecivel = new JRadioButton("Este lote é perecível?");
        painelFormulario.add(radioPerecivel, gbc);

        gbc.gridy = 5; gbc.gridwidth = 1;
        gbc.gridx = 0; labelDataValidade = new JLabel("Data de Validade (dd/mm/aaaa):");
        painelFormulario.add(labelDataValidade, gbc);

        gbc.gridx = 1; campoDataValidade = new JTextField(20);
        painelFormulario.add(campoDataValidade, gbc);

        // Esconde os campos de data de validade por padrão.
        labelDataValidade.setVisible(false);
        campoDataValidade.setVisible(false);

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados do Lote"));
        return scrollPane;
    }

    /**
     * Cria o painel inferior (Sul) com o botão de ação "Adicionar Lote".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoAdicionar = new JButton("Adicionar Lote");
        botaoAdicionar.setPreferredSize(new Dimension(150, 30));
        
        // Adiciona a lógica principal de criação e adição do lote.
        botaoAdicionar.addActionListener(e -> {
            try {
                // 1. Coleta e valida os dados do formulário.
                String codigoBarras = campoCodigoBarras.getText().trim();
                if (codigoBarras.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O campo 'Código de Barras' é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double quantidade = Double.parseDouble(campoQuantidade.getText().trim());
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 2. Busca o produto no catálogo.
                AppContext app = AppContext.getInstance();
                Produto produtoDoLote = app.getDados().catalogo.buscarProduto(codigoBarras);
                if (produtoDoLote == null) {
                    throw new ProdutoNaoCadastradoException("Nenhum produto encontrado com o código de barras informado.");
                }

                // 3. CRIAÇÃO POLIMÓRFICA DO OBJETO LOTE
                Lote novoLote; // A variável é do tipo abstrato 'Lote'.
                if (radioPerecivel.isSelected()) {
                    // Se for perecível, valida e converte a data e instancia LotePerecivel.
                    String textoData = campoDataValidade.getText().trim();
                    if (textoData.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "A data de validade é obrigatória para lotes perecíveis.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataValidade = LocalDate.parse(textoData, formatador);
                    novoLote = new LotePerecivel(produtoDoLote, quantidade, dataValidade);
                } else {
                    // Se não, instancia LoteNaoPerecivel.
                    novoLote = new LoteNaoPerecivel(produtoDoLote, quantidade);
                }

                // 4. Define os dados restantes e adiciona ao estoque.
                String fornecedor = campoFornecedor.getText().trim();
                double custo = Double.parseDouble(campoCusto.getText().trim().replace(",", "."));
                novoLote.setFornecedor(fornecedor.isEmpty() ? "Não informado" : fornecedor);
                novoLote.setCustoDoLote(custo);

                app.getDados().estoque.adicionarLote(novoLote);
                JOptionPane.showMessageDialog(this, "Lote adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // 5. Navega de volta para a tela de estoque.
                new TelaEstoque().setVisible(true);
                this.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "A quantidade e o custo devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/mm/aaaa.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (ProdutoNaoCadastradoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelSul.add(botaoAdicionar);
        return painelSul;
    }

    /**
     * Associa os listeners aos componentes interativos da janela.
     */
    private void adicionarListeners() {
        // Listener para o radio button que controla a visibilidade dos campos de data.
        radioPerecivel.addActionListener(e -> {
            boolean selecionado = radioPerecivel.isSelected();
            labelDataValidade.setVisible(selecionado);
            campoDataValidade.setVisible(selecionado);
        });
    }
}