package stokos.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import stokos.model.Grandeza;
import stokos.persistence.*;
import stokos.service.*;


/**
 * Janela para o cadastro de um novo tipo de produto no catálogo.
 * Layout redesenhado para maior flexibilidade.
 */
public class TelaCadastrarProduto extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoNome;
    private JComboBox<Grandeza> comboGrandeza; // Alterado de RadioButton para ComboBox
    private JTextField campoPreco;
    private JTextField campoLocal; // Novo campo
    private JButton botaoCadastrar;
    private JTextField campoEstoqueMinimo;

    // --- Construtor ---
    // Removi os parâmetros por enquanto, já que o foco é o design.
    // Adicionaremos a lógica de volta quando precisarmos.
    public TelaCadastrarProduto() {
        super("Cadastrar Novo Produto"); // Título da janela

        configurarJanela();
        inicializarComponentes();
        // Não vamos adicionar a lógica dos botões (Listeners) por enquanto.
    }

    // --- Métodos de Configuração da Janela ---
    private void configurarJanela() {
        this.setSize(500, 400); // Ajustei o tamanho para o novo layout
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centraliza na tela
        this.setLayout(new BorderLayout(10, 10)); // Layout principal com espaçamento
    }

    private void inicializarComponentes() {
        // Adiciona os painéis nas posições corretas do BorderLayout
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelFormularioScrollable(), BorderLayout.CENTER);
        this.add(criarPainelSul(), BorderLayout.SOUTH);
    }

    /**
     * Cria o painel superior com o botão "Voltar".
     * @return JPanel configurado.
     */
    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alinha à esquerda
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> 
        {
            new TelaProdutos().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel do formulário e o coloca dentro de um JScrollPane.
     * @return JScrollPane contendo o formulário.
     */
    private JScrollPane criarPainelFormularioScrollable() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem interna

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre os componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST; // Alinha componentes à esquerda

        // Linha 0: Código de Barras
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(new JLabel("Código de Barras:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Permite que o campo de texto estique horizontalmente
        campoCodigoBarras = new JTextField(20);
        painelFormulario.add(campoCodigoBarras, gbc);

        // Linha 1: Nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0; // Reseta o peso
        painelFormulario.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campoNome = new JTextField(20);
        painelFormulario.add(campoNome, gbc);

        // Linha 2: Grandeza (usando JComboBox)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Grandeza:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        // O JComboBox é preenchido com os valores do nosso Enum 'Grandeza'
        comboGrandeza = new JComboBox<>(Grandeza.values());
        painelFormulario.add(comboGrandeza, gbc);

        // Linha 3: Preço Unitário
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Preço Unitário:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campoPreco = new JTextField(20);
        painelFormulario.add(campoPreco, gbc);

        // Linha 4: Local (novo campo)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Local:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campoLocal = new JTextField(20);
        painelFormulario.add(campoLocal, gbc);

        // Linha 5
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Estoque Mínimo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        campoEstoqueMinimo = new JTextField(20);
        painelFormulario.add(campoEstoqueMinimo, gbc);

        // Envolve o painel do formulário em um JScrollPane
        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Formulário de Cadastro")); // Título para a área de rolagem

        return scrollPane;
    }

    /**
     * Cria o painel inferior com o botão "Cadastrar".
     * @return JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Alinha ao centro
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.addActionListener(e ->
        {
            AppContext app = AppContext.getInstance();
            CatalogoDeProdutos catalogo = app.getDados().catalogo;

            try { // Envolver em try-catch para tratar erros de conversão
                // pega as informações do formulário
                String codigoDeBarras = campoCodigoBarras.getText();
                String nomeDoProduto = campoNome.getText();
                double precoUnitario = Double.parseDouble(campoPreco.getText());
                Grandeza grandeza = (Grandeza) comboGrandeza.getSelectedItem();
                
                // --- CAPTURAR O NOVO VALOR ---
                double estoqueMinimo = 0;
                String textoEstoqueMinimo = campoEstoqueMinimo.getText().trim();
                if (!textoEstoqueMinimo.isEmpty()) {
                    estoqueMinimo = Double.parseDouble(textoEstoqueMinimo);
                }

                Produto novoProduto = new Produto(codigoDeBarras, nomeDoProduto, precoUnitario, grandeza);
                novoProduto.setEstoqueMinimo(estoqueMinimo); // --- DEFINIR O VALOR NO PRODUTO ---
                novoProduto.setCategoria(campoLocal.getText()); // Aproveitando o campo local como categoria

                // tenta cadastrar o produto
                catalogo.cadastrarProduto(novoProduto);

                // avisa o usuário que deu certo
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!"); 

                // fecha a janela
                 new TelaProdutos().setVisible(true);
                this.dispose();

            } catch (ProdutoJaCadastradoException exc) {
                JOptionPane.showMessageDialog(this, "Produto já cadastrado");
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(this, "Preço e Estoque Mínimo devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });


    

        botaoCadastrar.setPreferredSize(new Dimension(150, 30)); // Tamanho preferido para o botão
        painelSul.add(botaoCadastrar);
        return painelSul;
    }
}