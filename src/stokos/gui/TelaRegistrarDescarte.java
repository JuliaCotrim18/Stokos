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

/**
 * Representa a tela de registro de descarte de produtos.
 * Esta tela permite que o usuário registre o descarte de produtos do estoque,
 * informando o código de barras, quantidade e motivo (opcional).
 * 
 * CONCEITO DE DESIGN: SEPARAÇÃO DA CONSTRUÇÃO DA UI
 * A classe é organizada em métodos privados, cada um responsável por criar uma
 * parte específica da interface (ex: `criarPainelNorte`, `criarPainelFormulario`).
 * Essa abordagem torna o código mais limpo, legível e fácil de manter.
 */

public class TelaRegistrarDescarte extends JFrame {

    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoQuantidade;
    private JTextField campoMotivo;
    private JButton botaoRegistrarDescarte;

    
    /**
     * Construtor da tela de registro de descarte.
     * Orquestra a configuração da janela e o carregamento dos componentes.
     */
    public TelaRegistrarDescarte() {
        super("Registrar Descarte de Produto");
        configurarJanela();
        inicializarComponentes();
    }


    /**
     * Configura as propriedades essenciais da janela (JFrame).
     * Define tamanho, comportamento de fechamento e layout.
     */
    private void configurarJanela() {
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
    }


    /**
     * Inicializa e organiza os painéis principais dentro da janela.
     * Adiciona o painel norte, o formulário de descarte e o painel sul com o botão de registro.
     */
    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelFormulario(), BorderLayout.CENTER);
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
     * Cria o painel central que contém o formulário de registro de descarte.
     * Este painel inclui campos para código de barras, quantidade e motivo do descarte.
     * @return Um JScrollPane contendo o painel do formulário.
     */
    private JScrollPane criarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Código de Barras do Produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        campoCodigoBarras = new JTextField(20);
        painelFormulario.add(campoCodigoBarras, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelFormulario.add(new JLabel("Quantidade a Descartar:"), gbc);
        gbc.gridx = 1;
        campoQuantidade = new JTextField(20);
        painelFormulario.add(campoQuantidade, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Motivo (opcional):"), gbc);
        gbc.gridx = 1;
        campoMotivo = new JTextField(20);
        painelFormulario.add(campoMotivo, gbc);

        JScrollPane scrollPane = new JScrollPane(painelFormulario);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados do Descarte"));
        return scrollPane;
    }


    /**
     * Cria o painel inferior (Sul) com o botão de ação "Registrar Descarte".
     * Este painel contém a lógica para registrar o descarte de produtos.
     * @return O JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoRegistrarDescarte = new JButton("Registrar Descarte");

        botaoRegistrarDescarte.addActionListener(e -> {
            try {
                String codigoBarras = campoCodigoBarras.getText().trim();
                double quantidade = Double.parseDouble(campoQuantidade.getText().trim().replace(',', '.'));

                if (codigoBarras.isEmpty() || quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "Código de Barras e Quantidade são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Estoque estoque = AppContext.getInstance().getDados().estoque;
                estoque.registrarDescarte(codigoBarras, quantidade);

                JOptionPane.showMessageDialog(this, "Descarte registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                campoCodigoBarras.setText("");
                campoQuantidade.setText("");
                campoMotivo.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (ProdutoNaoCadastradoException | QuantidadeInsuficienteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Lógica", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelSul.add(botaoRegistrarDescarte);
        return painelSul;
    }
}