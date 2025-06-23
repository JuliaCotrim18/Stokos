// TelaCadastrarProduto.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Janela para o cadastro de um novo tipo de produto no catálogo.
 */
public class TelaCadastrarProduto extends JFrame {

    // --- Atributos ---
    private DadosDoSistema dados;
    private ServicoDeArmazenamento servicoArmazenamento;

    // Componentes da UI
    private JTextField campoCodigoBarras;
    private JTextField campoNome;
    private ButtonGroup grupoGrandeza;
    private JRadioButton radioUnidade, radioPeso, radioVolume;
    private JTextField campoPreco;
    private JButton botaoCadastrar;

    // --- Construtor ---
    public TelaCadastrarProduto(DadosDoSistema dados, Usuario usuario)
     {
        super("Cadastrar Novo Produto");
        this.dados = dados;
        this.servicoArmazenamento = new ArmazenamentoEmArquivo("dados.stk");

        configurarJanela();
        inicializarComponentes();
        adicionarListeners();
    }

    // --- Métodos de Configuração ---
    private void configurarJanela() {
        this.setSize(450, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Correto, como você disse
        this.setLocationRelativeTo(null); // Centraliza
    }

    private void inicializarComponentes() {
        // Usando GridBagLayout para um formulário mais flexível e alinhado
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre os componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Código de Barras
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Código de Barras:"), gbc);
        gbc.gridx = 1;
        campoCodigoBarras = new JTextField(20);
        painel.add(campoCodigoBarras, gbc);

        // Linha 1: Nome do Produto
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Nome do Produto:"), gbc);
        gbc.gridx = 1;
        campoNome = new JTextField(20);
        painel.add(campoNome, gbc);

        // Linha 2: Grandeza (Radio Buttons)
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(new JLabel("Grandeza:"), gbc);
        gbc.gridx = 1;
        radioUnidade = new JRadioButton("Unidade", true); // Pré-selecionado
        radioPeso = new JRadioButton("Peso (kg)");
        radioVolume = new JRadioButton("Volume (L)");
        grupoGrandeza = new ButtonGroup(); // Agrupa para que só um possa ser selecionado
        grupoGrandeza.add(radioUnidade);
        grupoGrandeza.add(radioPeso);
        grupoGrandeza.add(radioVolume);
        JPanel painelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Sub-painel para os radios
        painelRadio.add(radioUnidade);
        painelRadio.add(radioPeso);
        painelRadio.add(radioVolume);
        painel.add(painelRadio, gbc);

        // Linha 3: Preço Unitário
        gbc.gridx = 0;
        gbc.gridy = 3;
        painel.add(new JLabel("Preço Unitário:"), gbc);
        gbc.gridx = 1;
        campoPreco = new JTextField(20);
        painel.add(campoPreco, gbc);

        // Linha 4: Botão Cadastrar
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER; // Centraliza o botão
        botaoCadastrar = new JButton("Cadastrar");
        painel.add(botaoCadastrar, gbc);

        this.add(painel);
    }

    // --- Event Handling ---
    private void adicionarListeners() {
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método que contém a lógica do cadastro
                realizarCadastro();
            }
        });
    }

    private void realizarCadastro() {
        // --- COLETA E VALIDAÇÃO DOS DADOS DO FORMULÁRIO ---
        String codigoBarras = campoCodigoBarras.getText().trim();
        String nome = campoNome.getText().trim();
        String precoStr = campoPreco.getText().trim().replace(",", ".");

        if (codigoBarras.isEmpty() || nome.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- LÓGICA DE CADASTRO ---
        try {
            // Converte o preço para double
            double preco = Double.parseDouble(precoStr);

            // Verifica se o produto já existe no catálogo, como você especificou
            if (dados.catalogo.verificaProdutoCadastrado(codigoBarras)) {
                throw new ProdutoJaCadastradoException("Já existe um produto com o código de barras: " + codigoBarras);
            }

            // Determina a grandeza selecionada
            Grandeza grandeza;
            if (radioPeso.isSelected()) {
                grandeza = Grandeza.PESO;
            } else if (radioVolume.isSelected()) {
                grandeza = Grandeza.VOLUME;
            } else {
                grandeza = Grandeza.UNIDADE;
            }

            // Cria o novo objeto Produto (sem fornecedor, como você disse)
            Produto novoProduto = new Produto(nome, preco, grandeza, codigoBarras);

            // Adiciona ao catálogo
            dados.catalogo.cadastrarProduto(novoProduto);
            
            // Salva o novo estado do sistema, como você especificou
            // (Supondo que CAMINHO_ARMAZENAMENTO seja acessível ou passado)
            servicoArmazenamento.salvarDados(dados);

            // Exibe mensagem de sucesso e fecha a janela
            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O preço deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (ProdutoJaCadastradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}