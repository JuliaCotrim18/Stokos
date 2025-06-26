package stokos.gui;

import javax.swing.*;
import java.awt.*;
import stokos.AppContext; // Para encontrar a classe AppContext
import stokos.model.Cargo; // Para encontrar o Enum Cargo
import stokos.model.Produto;
import stokos.model.CatalogoDeProdutos;
import stokos.exception.ProdutoNaoCadastradoException;

public class TelaProdutos extends JFrame {
    // --- Atributos de Componentes da UI ---
    private JButton botaoCadastrarNovoProduto;
    private JTextField campoPesquisarProduto;
    private JButton botaoPesquisarProduto;
    
    // Campos de texto individuais para facilitar a edição
    private JTextField campoId, campoNome, campoPreco, campoCodBarras, campoCategoria;
    
    private JButton botaoAlterarDados;
    private JButton botaoRemoverProduto;
    private JButton botaoConfirmarAlteracoes; // Novo botão, inicialmente invisível
    private JButton botaoVoltar;

    // Guarda o produto que está sendo exibido/editado
    private Produto produtoEmExibicao;

    public TelaProdutos() {
        super("Stokos - Gerenciamento de Produtos");
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(criarPainelNorte(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelCentro(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelSul(), BorderLayout.SOUTH);

        this.add(painelPrincipal);
    }

    private JPanel criarPainelNorte() {
        // ... (código do painel norte permanece o mesmo, por enquanto sem ação)
        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(new BoxLayout(painelNorte, BoxLayout.Y_AXIS));
        botaoCadastrarNovoProduto = new JButton("Cadastrar Novo Produto no Catálogo");
        botaoCadastrarNovoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoCadastrarNovoProduto.addActionListener(e -> {
            // Aqui você pode abrir a tela de cadastro de produto
            new TelaCadastrarProduto().setVisible(true);
        });


        painelNorte.add(botaoCadastrarNovoProduto);
        painelNorte.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelPesquisa.add(new JLabel("Pesquisar por Código:"));
        campoPesquisarProduto = new JTextField(25);
        painelPesquisa.add(campoPesquisarProduto);
        botaoPesquisarProduto = new JButton("Pesquisar");
        botaoPesquisarProduto.addActionListener(e -> 
        {
            // Este código vai dentro do ActionListener do seu botaoPesquisarProduto na classe TelaProdutos
            
            // 1. Obter o texto do campo de pesquisa
            String codigoParaBuscar = campoPesquisarProduto.getText().trim();
            
            // Validação simples para não pesquisar em branco
            if (codigoParaBuscar.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Por favor, digite um código de barras para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 2. Aceder ao catálogo e realizar a busca
            CatalogoDeProdutos catalogo = AppContext.getInstance().getDados().catalogo;
            Produto produtoEncontrado = catalogo.buscarProduto(codigoParaBuscar);
            
            // 3. Verificar o resultado da busca
            if (produtoEncontrado != null) 
            {
                // SUCESSO! O produto foi encontrado.
                
                // Agora, preenchemos os campos de texto da tela com os dados do produto
                campoId.setText(String.valueOf(produtoEncontrado.getId()));
                campoNome.setText(produtoEncontrado.getNomeDoProduto());
                campoCodBarras.setText(produtoEncontrado.getCodigoDeBarras());
                campoPreco.setText(String.valueOf(produtoEncontrado.getPrecoUnitario()));
                campoCategoria.setText(produtoEncontrado.getCategoria());
                
                // Opcional: Guardar a referência do produto que está a ser exibido
                // para ser usado por outros botões (como "Alterar" ou "Remover").
                this.produtoEmExibicao = produtoEncontrado; 
            
            } else 
            {
                // ERRO! O produto não foi encontrado.
                
                // Limpar os campos e avisar o utilizador
                campoId.setText("");
                campoNome.setText("");
                campoCodBarras.setText("");
                campoPreco.setText("");
                campoCategoria.setText("");
                this.produtoEmExibicao = null; // Limpar a referência
                
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado com o código: " + codigoParaBuscar, "Erro de Busca", JOptionPane.ERROR_MESSAGE);
            }
        });
        painelPesquisa.add(botaoPesquisarProduto);
        painelNorte.add(painelPesquisa);
        return painelNorte;
    }

    private JScrollPane criarPainelCentro() {
        // Usamos GridLayout para organizar os campos em formato de formulário
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

        // Coloca o formulário dentro de um painel de rolagem, como você sugeriu
        return new JScrollPane(painelFormulario);
    }

    private JPanel criarPainelSul() {
        // O painel sul agora usa BorderLayout para alinhar os botões
        JPanel painelSul = new JPanel(new BorderLayout());

        // Botão Voltar à esquerda
        botaoVoltar = new JButton("Voltar ao Menu");
        botaoVoltar.addActionListener(e -> {
            new TelaPrincipal().setVisible(true);
            this.dispose();
        });
        painelSul.add(botaoVoltar, BorderLayout.WEST);

        // Painel para os botões de ação à direita
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botaoAlterarDados = new JButton("Alterar Dados");
        botaoRemoverProduto = new JButton("Remover Produto do Catálogo");
        botaoConfirmarAlteracoes = new JButton("Confirmar Alterações");
        botaoConfirmarAlteracoes.setVisible(false); // Começa invisível

        botaoConfirmarAlteracoes.addActionListener(e -> 
        {
            if (produtoEmExibicao == null) 
            {
                JOptionPane.showMessageDialog(this, "Nenhum produto está selecionado para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try 
            {
                // 1. Coletar os novos dados da tela
                String novoNome = campoNome.getText().trim();
                String novoCodBarras = campoCodBarras.getText().trim();
                String novaCategoria = campoCategoria.getText().trim();
                double novoPreco = Double.parseDouble(campoPreco.getText().trim());

                // 2. Validar para não deixar campos essenciais em branco
                if (novoNome.isEmpty() || novoCodBarras.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e Código de Barras não podem ser vazios.", "Dados Inválidos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 3. Atualizar o objeto Produto
                produtoEmExibicao.setNomeDoProduto(novoNome);
                produtoEmExibicao.setCodigoDeBarras(novoCodBarras);
                produtoEmExibicao.setCategoria(novaCategoria);
                produtoEmExibicao.setPrecoUnitario(novoPreco);

                // 4. Feedback para o usuário e sair do modo de edição
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                habilitarModoEdicao(false);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O preço deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro ao atualizar o produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

        });

        botaoRemoverProduto.addActionListener( e -> 
        {
            if (produtoEmExibicao == null) {
        JOptionPane.showMessageDialog(this, "Pesquise e selecione um produto antes de remover.", "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Pedir confirmação
    int resposta = JOptionPane.showConfirmDialog(
        this,
        "Tem certeza que deseja remover o produto '" + produtoEmExibicao.getNomeDoProduto() + "' do catálogo?\nEsta ação não pode ser desfeita.",
        "Confirmar Remoção",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (resposta == JOptionPane.YES_OPTION) {
        try {
            // Remover o produto do catálogo
            AppContext.getInstance().getDados().catalogo.removerProduto(produtoEmExibicao.getCodigoDeBarras());

            // Limpar a tela e a referência
            campoId.setText("");
            campoNome.setText("");
            campoCodBarras.setText("");
            campoPreco.setText("");
            campoCategoria.setText("");
            produtoEmExibicao = null;

            JOptionPane.showMessageDialog(this, "Produto removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (ProdutoNaoCadastradoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro de Remoção", JOptionPane.ERROR_MESSAGE);
        }
    }

        });

        // --- LÓGICA DE PERMISSÃO ---
        AppContext app = AppContext.getInstance();
        if (app.getUsuarioLogado().getCargo() != Cargo.CEO) {
            botaoAlterarDados.setEnabled(false);
            botaoRemoverProduto.setEnabled(false);
        }
        
        // --- AÇÃO PARA ENTRAR NO "MODO DE EDIÇÃO" ---
        botaoAlterarDados.addActionListener(e -> {
            // Só habilita a edição se um produto estiver sendo exibido
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
     * Habilita ou desabilita o "modo de edição" dos campos do formulário.
     * @param habilitar true para habilitar a edição, false para desabilitar.
     */
    private void habilitarModoEdicao(boolean habilitar) {
        // Apenas os campos que podem ser alterados
        campoNome.setEditable(habilitar);
        campoPreco.setEditable(habilitar);
        campoCodBarras.setEditable(habilitar);
        campoCategoria.setEditable(habilitar);
        
        // Mostra ou esconde o botão de confirmar
        botaoConfirmarAlteracoes.setVisible(habilitar);
        // Desabilita o botão "Alterar" para evitar cliques duplos
        botaoAlterarDados.setEnabled(!habilitar);
    }
}