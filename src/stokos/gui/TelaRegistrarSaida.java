package stokos.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import stokos.AppContext;
import stokos.model.Estoque;
import stokos.exception.*;
import stokos.model.HistoricoDeVendas;

/**
 * Representa a janela para registrar a saída (venda) de um produto do estoque.
 * A tela coleta o código do produto e a quantidade vendida, e delega a lógica
 * de baixa do estoque e registro da venda para as classes de modelo.
 */
public class TelaRegistrarSaida extends JFrame {

    // --- Atributos de Componentes da UI ---
    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoQuantidade;
    private JTextField campoComprador; // Campo opcional para registro.
    private JButton botaoRegistrarVenda;

    /**
     * Construtor da tela de registro de saída.
     */
    public TelaRegistrarSaida() {
        super("Registrar Saída de Produto do Estoque");
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades essenciais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(550, 400);
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
            new TelaEstoque().setVisible(true); // Retorna para a tela de gerenciamento de estoque.
            this.dispose();
        });
        painelNorte.add(botaoVoltar);
        return painelNorte;
    }

    /**
     * Cria o painel central com o formulário para os dados da venda.
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
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Código de Barras do Produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        campoCodigoBarras = new JTextField(20);
        painelFormulario.add(campoCodigoBarras, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelFormulario.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        campoQuantidade = new JTextField(20);
        painelFormulario.add(campoQuantidade, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Comprador (opcional):"), gbc);
        gbc.gridx = 1;
        campoComprador = new JTextField(20);
        painelFormulario.add(campoComprador, gbc);

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados da Saída"));
        return scrollPane;
    }

    /**
     * Cria o painel inferior (Sul) com o botão de ação "Registrar Venda".
     * @return O JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoRegistrarVenda = new JButton("Registrar Venda");

        // Adiciona o listener que contém a lógica principal da tela.
        botaoRegistrarVenda.addActionListener(e -> {
            try {
                // 1. Coleta e valida os dados de entrada do usuário.
                String codigoBarras = campoCodigoBarras.getText().trim();
                double quantidade = Double.parseDouble(campoQuantidade.getText().trim().replace(',', '.'));

                if (codigoBarras.isEmpty() || quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "Código de Barras e Quantidade são obrigatórios e a quantidade deve ser positiva.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 2. Acessa os dados e serviços necessários através do AppContext.
                AppContext app = AppContext.getInstance();
                Estoque estoque = app.getDados().estoque;
                HistoricoDeVendas historico = app.getDados().historicoDeVendas;

                // 3. Delega a lógica de negócio complexa para o método `registrarVenda` da classe Estoque.
                // A tela não sabe como a baixa do estoque é feita (FIFO/FEFO), apenas invoca a operação.
                estoque.registrarVenda(codigoBarras, quantidade, historico);

                // 4. Fornece feedback de sucesso e limpa os campos para a próxima venda.
                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                campoCodigoBarras.setText("");
                campoQuantidade.setText("");
                campoComprador.setText("");

            } catch (NumberFormatException ex) {
                // Trata erros de conversão de texto para número.
                JOptionPane.showMessageDialog(this, "A quantidade deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (ProdutoNaoCadastradoException | QuantidadeInsuficienteException ex) {
                // Trata exceções de regra de negócio de forma unificada, exibindo a mensagem específica da exceção.
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Lógica", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Captura genérica para qualquer outro erro inesperado.
                JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelSul.add(botaoRegistrarVenda);
        return painelSul;
    }
}