package stokos.gui;

import javax.swing.*;
import java.awt.*;
import stokos.AppContext;
import stokos.model.Cargo;
import stokos.model.Produto;
import stokos.model.CatalogoDeProdutos;
import stokos.model.Estoque;
import stokos.exception.LoteNaoVazioException;
import stokos.exception.ProdutoNaoCadastradoException;

/**
 * Representa a tela de gerenciamento de produtos do catálogo.
 * Permite ao usuário pesquisar, visualizar, alterar e remover produtos, além de
 * fornecer um atalho para a tela de cadastro de novos produtos. A tela também
 * implementa um controle de permissões para as funcionalidades críticas.
 */
public class TelaProdutos extends JFrame {
    // --- Atributos de Componentes da UI ---
    private JButton botaoCadastrarNovoProduto;
    private JTextField campoPesquisarProduto;
    private JButton botaoPesquisarProduto;
    private JTextField campoId, campoNome, campoPreco, campoCodBarras, campoCategoria;
    private JButton botaoAlterarDados;
    private JButton botaoRemoverProduto;
    private JButton botaoConfirmarAlteracoes;
    private JButton botaoVoltar;

    // Atributo para manter a referência do produto atualmente carregado na tela.
    private Produto produtoEmExibicao;

    /**
     * Construtor da tela de gerenciamento de produtos.
     */
    public TelaProdutos() {
        super("Stokos - Gerenciamento de Produtos");
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades essenciais da janela (JFrame).
     */
    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    /**
     * Inicializa e organiza os painéis principais dentro da janela.
     */
    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(criarPainelNorte(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelCentro(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelSul(), BorderLayout.SOUTH);

        this.add(painelPrincipal);
    }

    /**
     * Cria o painel superior (Norte), que contém o botão para cadastrar
     * um novo produto e a funcionalidade de pesquisa.
     * @return O JPanel configurado.
     */
    private JPanel criarPainelNorte() {
        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(new BoxLayout(painelNorte, BoxLayout.Y_AXIS));

        // Botão para navegar para a tela de cadastro.
        botaoCadastrarNovoProduto = new JButton("Cadastrar Novo Produto no Catálogo");
        botaoCadastrarNovoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCadastrarNovoProduto.addActionListener(e -> {
            new TelaCadastrarProduto().setVisible(true);
            this.dispose();
        });
        painelNorte.add(botaoCadastrarNovoProduto);
        painelNorte.add(Box.createRigidArea(new Dimension(0, 20)));

        // Painel de pesquisa.
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelPesquisa.add(new JLabel("Pesquisar por Código:"));
        campoPesquisarProduto = new JTextField(25);
        painelPesquisa.add(campoPesquisarProduto);
        botaoPesquisarProduto = new JButton("Pesquisar");

        // Lógica de busca do produto.
        botaoPesquisarProduto.addActionListener(e -> {
            String codigoParaBuscar = campoPesquisarProduto.getText().trim();
            if (codigoParaBuscar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um código de barras para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Delega a busca para a classe de catálogo.
            CatalogoDeProdutos catalogo = AppContext.getInstance().getDados().catalogo;
            Produto produtoEncontrado = catalogo.buscarProduto(codigoParaBuscar);

            // Preenche o formulário se o produto for encontrado, ou limpa e avisa o usuário.
            if (produtoEncontrado != null) {
                campoId.setText(String.valueOf(produtoEncontrado.getId()));
                campoNome.setText(produtoEncontrado.getNomeDoProduto());
                campoCodBarras.setText(produtoEncontrado.getCodigoDeBarras());
                campoPreco.setText(String.valueOf(produtoEncontrado.getPrecoUnitario()));
                campoCategoria.setText(produtoEncontrado.getCategoria());
                this.produtoEmExibicao = produtoEncontrado; // Armazena a referência.
            } else {
                campoId.setText("");
                campoNome.setText("");
                campoCodBarras.setText("");
                campoPreco.setText("");
                campoCategoria.setText("");
                this.produtoEmExibicao = null; // Limpa a referência.
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado com o código: " + codigoParaBuscar, "Erro de Busca", JOptionPane.ERROR_MESSAGE);
            }
        });
        painelPesquisa.add(botaoPesquisarProduto);
        painelNorte.add(painelPesquisa);
        return painelNorte;
    }

    /**
     * Cria o painel central, que exibe os dados do produto pesquisado.
     * Os campos são, por padrão, não editáveis.
     * @return Um JScrollPane contendo o painel de dados.
     */
    private JScrollPane criarPainelCentro() {
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));

        campoId = new JTextField();
        campoId.setEditable(false);
        campoNome = new JTextField();
        campoNome.setEditable(false);
        campoPreco = new JTextField();
        campoPreco.setEditable(false);
        campoCodBarras = new JTextField();
        campoCodBarras.setEditable(false);
        campoCategoria = new JTextField();
        campoCategoria.setEditable(false);

        painelFormulario.add(new JLabel("ID:"));
        painelFormulario.add(campoId);
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);
        painelFormulario.add(new JLabel("Preço Unitário:"));
        painelFormulario.add(campoPreco);
        painelFormulario.add(new JLabel("Cód. Barras:"));
        painelFormulario.add(campoCodBarras);
        painelFormulario.add(new JLabel("Categoria:"));
        painelFormulario.add(campoCategoria);

        return new JScrollPane(painelFormulario);
    }

    /**
     * Cria o painel inferior (Sul) com os botões de ação (Voltar, Alterar, Remover, Confirmar).
     * @return O JPanel configurado.
     */
    private JPanel criarPainelSul() {
        JPanel painelSul = new JPanel(new BorderLayout());

        // Botão "Voltar" à esquerda.
        botaoVoltar = new JButton("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelSul.add(botaoVoltar, BorderLayout.WEST);

        // Painel para botões de ação à direita.
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botaoAlterarDados = new JButton("Alterar Dados");
        botaoRemoverProduto = new JButton("Remover Produto do Catálogo");
        botaoConfirmarAlteracoes = new JButton("Confirmar Alterações");
        botaoConfirmarAlteracoes.setVisible(false); // Fica invisível até o modo de edição ser ativado.

        // Lógica para confirmar as alterações feitas no produto.
        botaoConfirmarAlteracoes.addActionListener(e -> {
            if (produtoEmExibicao == null) {
                JOptionPane.showMessageDialog(this, "Nenhum produto está selecionado para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // Coleta e atualiza os dados no objeto 'produtoEmExibicao'.
                produtoEmExibicao.setNomeDoProduto(campoNome.getText().trim());
                produtoEmExibicao.setCodigoDeBarras(campoCodBarras.getText().trim());
                produtoEmExibicao.setCategoria(campoCategoria.getText().trim());
                produtoEmExibicao.setPrecoUnitario(Double.parseDouble(campoPreco.getText().trim()));

                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                habilitarModoEdicao(false); // Desativa o modo de edição.
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O preço deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Lógica para remover um produto do catálogo.
        botaoRemoverProduto.addActionListener(e -> {
            if (produtoEmExibicao == null) {
                JOptionPane.showMessageDialog(this, "Pesquise e selecione um produto antes de remover.", "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Pede confirmação ao usuário antes de uma ação destrutiva.
            int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o produto '" + produtoEmExibicao.getNomeDoProduto() + "'?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (resposta == JOptionPane.YES_OPTION) {
                try {
                    AppContext app = AppContext.getInstance();
                    // Passa o estoque para o método de remoção para validar se há lotes não vazios.
                    app.getDados().catalogo.removerProduto(produtoEmExibicao.getCodigoDeBarras(), app.getDados().estoque);
                    
                    // Limpa a tela após a remoção.
                    campoId.setText("");
                    campoNome.setText("");
                    campoCodBarras.setText("");
                    campoPreco.setText("");
                    campoCategoria.setText("");
                    produtoEmExibicao = null;

                    JOptionPane.showMessageDialog(this, "Produto removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (ProdutoNaoCadastradoException | LoteNaoVazioException ex) {
                    // Trata exceções específicas com mensagens claras.
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro de Remoção", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- CONTROLE DE ACESSO BASEADO NO CARGO DO USUÁRIO ---
        if (AppContext.getInstance().getUsuarioLogado().getCargo() != Cargo.CEO) {
            botaoAlterarDados.setEnabled(false);
            botaoRemoverProduto.setEnabled(false);
        }
        
        // Lógica para habilitar o modo de edição.
        botaoAlterarDados.addActionListener(e -> {
            if (produtoEmExibicao != null) {
                habilitarModoEdicao(true);
            } else {
                JOptionPane.showMessageDialog(this, "Pesquise e selecione um produto antes de alterar.", "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
            }
        });

        painelAcoes.add(botaoAlterarDados);
        painelAcoes.add(botaoRemoverProduto);
        painelAcoes.add(botaoConfirmarAlteracoes);
        
        painelSul.add(painelAcoes, BorderLayout.EAST);
        return painelSul;
    }
    
    /**
     * Alterna a interface entre o modo de visualização e o modo de edição.
     * @param habilitar `true` para entrar no modo de edição, `false` para sair.
     */
    private void habilitarModoEdicao(boolean habilitar) {
        // Torna os campos de texto editáveis ou não.
        campoNome.setEditable(habilitar);
        campoPreco.setEditable(habilitar);
        campoCodBarras.setEditable(habilitar);
        campoCategoria.setEditable(habilitar);
        
        // Exibe/oculta o botão "Confirmar Alterações" e habilita/desabilita o botão "Alterar Dados".
        botaoConfirmarAlteracoes.setVisible(habilitar);
        botaoAlterarDados.setEnabled(!habilitar);
    }
}