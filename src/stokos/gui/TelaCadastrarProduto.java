package stokos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import stokos.AppContext;
import stokos.exception.*;
import stokos.model.CatalogoDeProdutos;
import stokos.model.Produto;
import stokos.model.ProdutoComum;
import stokos.model.ProdutoComImposto;
import stokos.model.Grandeza;

/**
 * Representa a janela para o cadastro de um novo tipo de produto no catálogo.
 * Esta tela coleta todos os dados necessários e, de forma polimórfica, cria
 * o tipo correto de objeto Produto (`ProdutoComum` ou `ProdutoComImposto`).
 */
public class TelaCadastrarProduto extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras, campoNome, campoPreco, campoCategoria, campoEstoqueMinimo;
    private JComboBox<Grandeza> comboGrandeza;
    private JCheckBox checkTemImposto;
    private JLabel labelPercentualIcms;
    private JTextField campoPercentualIcms;
    private JButton botaoCadastrar;

    /**
     * Construtor da tela de cadastro de produto.
     */
    public TelaCadastrarProduto() {
        super("Cadastrar Novo Produto");
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades essenciais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(550, 480);
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
            new TelaProdutos().setVisible(true); // Retorna à tela de gerenciamento de produtos.
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel central com o formulário de cadastro de produtos.
     * Utiliza `GridBagLayout` para um alinhamento preciso dos componentes.
     * @return Um JScrollPane contendo o painel do formulário.
     */
    private JScrollPane criarPainelFormularioScrollable() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // GridBagConstraints (gbc) é usado para definir a posição e o comportamento
        // de cada componente dentro do GridBagLayout.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margem externa para cada componente.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz com que o componente preencha o espaço horizontal.
        gbc.anchor = GridBagConstraints.WEST; // Alinha os componentes à esquerda.

        // Adiciona os campos do formulário, um a um, configurando suas posições na grade.
        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("Código de Barras:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoCodigoBarras = new JTextField(20); painelFormulario.add(campoCodigoBarras, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoNome = new JTextField(20); painelFormulario.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; painelFormulario.add(new JLabel("Grandeza:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; comboGrandeza = new JComboBox<>(Grandeza.values()); painelFormulario.add(comboGrandeza, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; painelFormulario.add(new JLabel("Preço Unitário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoPreco = new JTextField(20); painelFormulario.add(campoPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; painelFormulario.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoCategoria = new JTextField(20); painelFormulario.add(campoCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0; painelFormulario.add(new JLabel("Estoque Mínimo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoEstoqueMinimo = new JTextField(20); painelFormulario.add(campoEstoqueMinimo, gbc);

        // Componentes para a lógica condicional de imposto.
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; // Ocupa duas colunas.
        checkTemImposto = new JCheckBox("Este produto possui imposto (ICMS)?");
        painelFormulario.add(checkTemImposto, gbc);

        gbc.gridy = 7; gbc.gridwidth = 1; // Volta a ocupar uma coluna.
        gbc.gridx = 0; labelPercentualIcms = new JLabel("Percentual de ICMS (ex: 0.18):");
        painelFormulario.add(labelPercentualIcms, gbc);

        gbc.gridx = 1; campoPercentualIcms = new JTextField(20);
        painelFormulario.add(campoPercentualIcms, gbc);

        // Esconde os campos de imposto por padrão.
        labelPercentualIcms.setVisible(false);
        campoPercentualIcms.setVisible(false);

        // Listener que exibe ou esconde os campos de imposto com base na seleção do JCheckBox.
        checkTemImposto.addActionListener(e -> {
            boolean selecionado = checkTemImposto.isSelected();
            labelPercentualIcms.setVisible(selecionado);
            campoPercentualIcms.setVisible(selecionado);
        });

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Formulário de Cadastro"));
        return scrollPane;
    }

    /**
     * Cria o painel inferior (Sul) com o botão de ação "Cadastrar".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoCadastrar = new JButton("Cadastrar");

        // Adiciona o listener que contém toda a lógica de cadastro do produto.
        botaoCadastrar.addActionListener(e -> {
            try {
                // 1. Coleta e valida os dados do formulário.
                AppContext app = AppContext.getInstance();
                CatalogoDeProdutos catalogo = app.getDados().catalogo;

                String codigoDeBarras = campoCodigoBarras.getText().trim();
                String nomeDoProduto = campoNome.getText().trim();
                double precoUnitario = Double.parseDouble(campoPreco.getText().trim().replace(',', '.'));
                Grandeza grandeza = (Grandeza) comboGrandeza.getSelectedItem();

                double estoqueMinimo = 0;
                if (!campoEstoqueMinimo.getText().trim().isEmpty()) {
                    estoqueMinimo = Double.parseDouble(campoEstoqueMinimo.getText().trim().replace(',', '.'));
                }

                // 2. CRIAÇÃO POLIMÓRFICA DO OBJETO PRODUTO
                // A variável é do tipo abstrato 'Produto'.
                Produto novoProduto;

                // A decisão de qual classe concreta instanciar (`ProdutoComImposto` ou `ProdutoComum`)
                // é feita com base na seleção do usuário na interface.
                // Isto é um exemplo prático de como o polimorfismo é aplicado na criação de objetos.
                if (checkTemImposto.isSelected()) {
                    double percentual = Double.parseDouble(campoPercentualIcms.getText().trim().replace(',', '.'));
                    novoProduto = new ProdutoComImposto(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza, percentual);
                } else {
                    novoProduto = new ProdutoComum(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);
                }

                // 3. Define os atributos restantes e cadastra no catálogo.
                novoProduto.setEstoqueMinimo(estoqueMinimo);
                novoProduto.setCategoria(campoCategoria.getText().trim());

                catalogo.cadastrarProduto(novoProduto);

                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                new TelaProdutos().setVisible(true);
                this.dispose();

            } catch (ProdutoJaCadastradoException exc) {
                // Tratamento de exceções específicas para feedback claro ao usuário.
                JOptionPane.showMessageDialog(this, "Erro: " + exc.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(this, "Os campos de preço, estoque e imposto devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                // Captura genérica para erros inesperados.
                JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + exc.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelSul.add(botaoCadastrar);
        return painelSul;
    }
}