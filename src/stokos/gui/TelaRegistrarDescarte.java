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

public class TelaRegistrarDescarte extends JFrame {

    private JButton botaoVoltar;
    private JTextField campoCodigoBarras;
    private JTextField campoQuantidade;
    private JTextField campoMotivo;
    private JButton botaoRegistrarDescarte;

    public TelaRegistrarDescarte() {
        super("Registrar Descarte de Produto");
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        this.setSize(550, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        this.add(criarPainelNorte(), BorderLayout.NORTH);
        this.add(criarPainelFormulario(), BorderLayout.CENTER);
        this.add(criarPainelSul(), BorderLayout.SOUTH);
    }

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