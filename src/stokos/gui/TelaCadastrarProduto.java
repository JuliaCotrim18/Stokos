package stokos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox; // Import necessário
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
import stokos.model.ProdutoComum; // Import da nova classe
import stokos.model.ProdutoComImposto; // Import da nova classe
import stokos.model.Grandeza;


/**
 * Janela para o cadastro de um novo tipo de produto no catálogo.
 * Layout redesenhado para maior flexibilidade e polimorfismo.
 */
public class TelaCadastrarProduto extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoNome;
    private JComboBox<Grandeza> comboGrandeza;
    private JTextField campoPreco;
    private JTextField campoCategoria; // Renomeado de campoLocal para mais clareza
    private JTextField campoEstoqueMinimo;
    private JCheckBox checkTemImposto;
    private JLabel labelPercentualIcms;
    private JTextField campoPercentualIcms;
    private JButton botaoCadastrar;

    public TelaCadastrarProduto() {
        super("Cadastrar Novo Produto");
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        this.setSize(550, 480); // Aumentei um pouco a altura para os novos campos
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
        botaoVoltar.addActionListener(e -> {
            new TelaProdutos().setVisible(true);
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

        // Linhas 0 a 3 (Cód. Barras, Nome, Grandeza, Preço) - Sem alterações
        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("Código de Barras:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoCodigoBarras = new JTextField(20); painelFormulario.add(campoCodigoBarras, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoNome = new JTextField(20); painelFormulario.add(campoNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; painelFormulario.add(new JLabel("Grandeza:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; comboGrandeza = new JComboBox<>(Grandeza.values()); painelFormulario.add(comboGrandeza, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; painelFormulario.add(new JLabel("Preço Unitário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoPreco = new JTextField(20); painelFormulario.add(campoPreco, gbc);

        // Linha 4: Categoria (antigo campoLocal)
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; painelFormulario.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoCategoria = new JTextField(20); painelFormulario.add(campoCategoria, gbc);
        
        // Linha 5: Estoque Mínimo
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0; painelFormulario.add(new JLabel("Estoque Mínimo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; campoEstoqueMinimo = new JTextField(20); painelFormulario.add(campoEstoqueMinimo, gbc);

        // Linha 6: CheckBox de Imposto
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        checkTemImposto = new JCheckBox("Este produto possui imposto (ICMS)?");
        painelFormulario.add(checkTemImposto, gbc);
        
        // Linha 7: Campo de Imposto (inicialmente invisível)
        gbc.gridy = 7; gbc.gridwidth = 1;
        gbc.gridx = 0; labelPercentualIcms = new JLabel("Percentual de ICMS (ex: 0.18):");
        painelFormulario.add(labelPercentualIcms, gbc);

        gbc.gridx = 1; campoPercentualIcms = new JTextField(20);
        painelFormulario.add(campoPercentualIcms, gbc);

        labelPercentualIcms.setVisible(false);
        campoPercentualIcms.setVisible(false);

        checkTemImposto.addActionListener(e -> {
            boolean selecionado = checkTemImposto.isSelected();
            labelPercentualIcms.setVisible(selecionado);
            campoPercentualIcms.setVisible(selecionado);
        });

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Formulário de Cadastro"));
        return scrollPane;
    }

    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoCadastrar = new JButton("Cadastrar");
        
        botaoCadastrar.addActionListener(e -> {
            try {
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
                
                Produto novoProduto; // A variável é do tipo abstrato

                // --- LÓGICA DE CRIAÇÃO POLIMÓRFICA ---
                if (checkTemImposto.isSelected()) {
                    double percentual = Double.parseDouble(campoPercentualIcms.getText().trim().replace(',', '.'));
                    novoProduto = new ProdutoComImposto(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza, percentual);
                } else {
                    novoProduto = new ProdutoComum(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);
                }

                // Define os atributos restantes
                novoProduto.setEstoqueMinimo(estoqueMinimo);
                novoProduto.setCategoria(campoCategoria.getText().trim());

                catalogo.cadastrarProduto(novoProduto);

                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                new TelaProdutos().setVisible(true);
                this.dispose();

            } catch (ProdutoJaCadastradoException exc) {
                JOptionPane.showMessageDialog(this, "Erro: " + exc.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(this, "Os campos de preço, estoque e imposto devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + exc.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelSul.add(botaoCadastrar);
        return painelSul;
    }
}