package stokos;

import stokos.model.*;
import stokos.service.ServicoDeAutenticacao;
import stokos.service.ServicoDeExportacao;
import stokos.exception.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe dedicada a realizar testes funcionais na lógica de negócio do sistema Stokos.
 * Estes testes são executados via console e não dependem da interface gráfica,
 * permitindo verificar se as regras de negócio estão funcionando corretamente.
 */
public class TesteFuncionalidades {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTES FUNCIONAIS DO STOKOS ---");

        // Obter a instância do AppContext, que gerencia todos os dados e serviços.
        AppContext app = AppContext.getInstance();
        
        // Limpar dados de execuções anteriores para garantir um ambiente de teste limpo.
        limparDados(app);

        // Executar a sequência de testes
        testeAutenticacaoDeUsuarios(app.getServicoDeAutenticacao());
        testeCadastroDeProdutos(app.getDados().catalogo);
        testeAdicaoDeLotes(app.getDados().catalogo, app.getDados().estoque);
        testeRegistroDeVenda(app.getDados().estoque, app.getDados().historicoDeVendas);
        testeAlertasDeEstoque(app);
        testeModificacaoDeProduto(app.getDados().catalogo);
        testeExportacaoCSV();
        testePersistencia(app);


        System.out.println("\n--- FIM DOS TESTES ---");
    }

    /**
     * Limpa os dados do sistema para garantir que os testes comecem de um estado conhecido.
     */
    public static void limparDados(AppContext app) {
        app.getDados().catalogo.getListaDeProdutos().clear();
        app.getDados().estoque.getLotes().clear();
        // Para limpar o histórico, uma abordagem seria ter um método clear na classe.
        // Por agora, vamos recriar o objeto.
        app.getDados().historicoDeVendas = new HistoricoDeVendas();
        System.out.println("-> Ambiente de teste limpo.");
    }

    /**
     * Testa o serviço de autenticação com um login válido, um inválido e um com senha errada.
     */
    public static void testeAutenticacaoDeUsuarios(ServicoDeAutenticacao servico) {
        System.out.println("\n[TESTE: AUTENTICAÇÃO DE USUÁRIOS]");
        
        // Teste de sucesso
        Usuario u1 = servico.autenticar("esther", "mc322");
        if (u1 != null && u1.getCargo() == Cargo.CEO) {
            System.out.println("SUCESSO: Usuário 'esther' autenticado como CEO.");
        } else {
            System.err.println("FALHA: Usuário 'esther' não autenticou corretamente.");
        }

        // Teste de falha (senha incorreta)
        Usuario u2 = servico.autenticar("athyrson", "senha_errada");
        if (u2 == null) {
            System.out.println("SUCESSO: Tentativa de login de 'athyrson' com senha errada foi bloqueada.");
        } else {
            System.err.println("FALHA: Usuário 'athyrson' com senha errada foi autenticado.");
        }

        // Teste de falha (usuário inexistente)
        Usuario u3 = servico.autenticar("fantasma", "123");
        if (u3 == null) {
            System.out.println("SUCESSO: Tentativa de login de usuário inexistente foi bloqueada.");
        } else {
            System.err.println("FALHA: Usuário inexistente foi autenticado.");
        }
    }

    /**
     * Testa o cadastro de produtos, incluindo o caso de sucesso e a tentativa de
     * cadastrar um produto duplicado.
     */
    public static void testeCadastroDeProdutos(CatalogoDeProdutos catalogo) {
        System.out.println("\n[TESTE: CADASTRO DE PRODUTOS]");
        try {
            Produto p1 = new ProdutoComum("789001", "Detergente Ypê", 2.50, Grandeza.UNIDADE);
            catalogo.cadastrarProduto(p1);
            System.out.println("SUCESSO: 'Detergente Ypê' cadastrado.");

            Produto p2 = new ProdutoComImposto("789002", "Picanha Friboi (kg)", 79.90, Grandeza.PESO, 0.18);
            catalogo.cadastrarProduto(p2);
            System.out.println("SUCESSO: 'Picanha Friboi' cadastrada.");

            System.out.println("\n-> Testando falha esperada (produto duplicado)...");
            Produto p3_duplicado = new ProdutoComum("789001", "Detergente Limpol", 2.30, Grandeza.UNIDADE);
            catalogo.cadastrarProduto(p3_duplicado);

        } catch (ProdutoJaCadastradoException e) {
            System.out.println("SUCESSO NO TESTE DE FALHA: Exceção capturada como esperado. Mensagem: " + e.getMessage());
        }
    }
    
    /**
     * Testa a adição de lotes ao estoque.
     */
    public static void testeAdicaoDeLotes(CatalogoDeProdutos catalogo, Estoque estoque) {
        System.out.println("\n[TESTE: ADIÇÃO DE LOTES AO ESTOQUE]");
        try {
            Produto detergente = catalogo.buscarProduto("789001");
            Lote lote1 = new LoteNaoPerecivel(detergente, 100);
            estoque.adicionarLote(lote1);
            System.out.println("SUCESSO: Lote de 100 unidades de 'Detergente' adicionado.");
        } catch (ProdutoNaoCadastradoException e) {
            System.err.println("ERRO INESPERADO NO TESTE: " + e.getMessage());
        }
    }

    /**
     * Testa o registro de uma venda.
     */
    public static void testeRegistroDeVenda(Estoque estoque, HistoricoDeVendas historico) {
        System.out.println("\n[TESTE: REGISTRO DE VENDA]");
        try {
            System.out.println("-> Estoque de Detergente ANTES da venda: " + estoque.getQuantidadeDisponivel("789001"));
            estoque.registrarVenda("789001", 10, historico);
            System.out.println("SUCESSO: Venda de 10 unidades de 'Detergente' registrada.");
            System.out.println("-> Estoque de Detergente DEPOIS da venda: " + estoque.getQuantidadeDisponivel("789001"));
            
            System.out.println("\n-> Testando falha esperada (quantidade insuficiente)...");
            estoque.registrarVenda("789001", 1000, historico);

        } catch (QuantidadeInsuficienteException e) {
            System.out.println("SUCESSO NO TESTE DE FALHA: Exceção capturada. Mensagem: " + e.getMessage());
        } catch (ProdutoNaoCadastradoException e) {
            System.err.println("ERRO INESPERADO NO TESTE: " + e.getMessage());
        }
    }

    /**
     * Testa a lógica de geração de alertas de estoque baixo e de vencimento próximo.
     */
    public static void testeAlertasDeEstoque(AppContext app) {
        System.out.println("\n[TESTE: GERAÇÃO DE ALERTAS DE ESTOQUE]");
        try {
            // Setup para o teste
            Produto iogurte = new ProdutoComum("789003", "Iogurte Natural", 4.00, Grandeza.UNIDADE);
            iogurte.setEstoqueMinimo(10); // Estoque mínimo de 10 unidades
            app.getDados().catalogo.cadastrarProduto(iogurte);
            
            // Lote com quantidade abaixo do mínimo E perto de vencer
            Lote loteIogurte = new LotePerecivel(iogurte, 5, java.time.LocalDate.now().plusDays(2)); // Vence em 2 dias
            app.getDados().estoque.adicionarLote(loteIogurte);

            // Lógica de geração de alertas (copiada da TelaAvisos)
            ArrayList<String> alertas = new ArrayList<>();
            for (Produto p : app.getDados().catalogo.getListaDeProdutos()) {
                if (p.getEstoqueMinimo() > 0 && app.getDados().estoque.getQuantidadeDisponivel(p.getCodigoDeBarras()) <= p.getEstoqueMinimo()) {
                    alertas.add("ESTOQUE BAIXO: " + p.getNomeDoProduto());
                }
            }
            for (Lote l : app.getDados().estoque.getLotes()) {
                if (l.estaPertoDeVencer()) { //
                    alertas.add("VENCIMENTO PRÓXIMO: " + l.getProduto().getNomeDoProduto());
                }
            }

            // Verificação
            if (alertas.contains("ESTOQUE BAIXO: Iogurte Natural") && alertas.contains("VENCIMENTO PRÓXIMO: Iogurte Natural")) {
                System.out.println("SUCESSO: Alertas de estoque baixo e vencimento próximo foram gerados corretamente.");
            } else {
                System.err.println("FALHA: Os alertas esperados não foram gerados. Alertas recebidos: " + alertas);
            }

        } catch (Exception e) {
            System.err.println("ERRO INESPERADO NO TESTE DE ALERTAS: " + e.getMessage());
        }
    }

    /**
     * Testa a modificação de um produto existente no catálogo.
     */
    public static void testeModificacaoDeProduto(CatalogoDeProdutos catalogo) {
        System.out.println("\n[TESTE: MODIFICAÇÃO DE PRODUTO]");
        Produto p = catalogo.buscarProduto("789001");
        if (p != null) {
            String nomeAntigo = p.getNomeDoProduto();
            p.setNomeDoProduto("Detergente Ypê SUPER");
            p.setPrecoUnitario(3.00);

            Produto p_modificado = catalogo.buscarProduto("789001");
            if (p_modificado.getNomeDoProduto().equals("Detergente Ypê SUPER") && p_modificado.getPrecoUnitario() == 3.00) {
                System.out.println("SUCESSO: Produto '" + nomeAntigo + "' foi alterado para '" + p_modificado.getNomeDoProduto() + "'.");
            } else {
                System.err.println("FALHA: A modificação do produto não foi refletida.");
            }
        } else {
            System.err.println("FALHA: Produto para modificação não encontrado.");
        }
    }
    
    /**
     * Testa a exportação de dados para um arquivo CSV e verifica seu conteúdo.
     */
    public static void testeExportacaoCSV() {
        System.out.println("\n[TESTE: EXPORTAÇÃO PARA CSV]");
        String caminhoTeste = "./relatorio_teste.csv";
        ServicoDeExportacao servico = new ServicoDeExportacao();
        
        // 1. Criar um modelo de tabela com dados de teste
        String[] colunas = {"Produto", "Preco"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        model.addRow(new Object[]{"Produto A", "10.0"});
        model.addRow(new Object[]{"Produto B", "20.5"});

        try {
            // 2. Exportar
            servico.exportarParaCSV(model, caminhoTeste);
            
            // 3. Ler e verificar o arquivo gerado
            try (BufferedReader reader = new BufferedReader(new FileReader(caminhoTeste))) {
                String header = reader.readLine();
                String row1 = reader.readLine();
                String row2 = reader.readLine();

                if ("Produto;Preco".equals(header) && "Produto A;10.0".equals(row1) && "Produto B;20.5".equals(row2)) {
                    System.out.println("SUCESSO: Arquivo CSV gerado e verificado com sucesso.");
                } else {
                    System.err.println("FALHA: O conteúdo do arquivo CSV está incorreto.");
                }
            }

        } catch (IOException e) {
            System.err.println("FALHA: Ocorreu um erro de I/O ao testar a exportação CSV: " + e.getMessage());
        }
    }

    /**
     * Testa o ciclo completo de salvar os dados em arquivo e carregá-los novamente.
     */
    public static void testePersistencia(AppContext app) {
        System.out.println("\n[TESTE: PERSISTÊNCIA DE DADOS (SALVAR/CARREGAR)]");
        try {
            // 1. Adicionar um produto único para o teste
            Produto produtoTeste = new ProdutoComum("999999", "Produto de Teste Persistencia", 99.99, Grandeza.UNIDADE);
            app.getDados().catalogo.cadastrarProduto(produtoTeste);

            // 2. Salvar os dados
            app.salvarDados();
            System.out.println("-> Dados salvos em arquivo.");

            // 3. Simular reinicialização: Limpar dados da memória e recarregar
            limparDados(app);
            System.out.println("-> Dados da memória limpos.");
            app.carregarDados();
            System.out.println("-> Dados carregados do arquivo.");

            // 4. Verificar se o produto de teste foi carregado
            Produto produtoCarregado = app.getDados().catalogo.buscarProduto("999999");
            if (produtoCarregado != null && produtoCarregado.getNomeDoProduto().equals("Produto de Teste Persistencia")) {
                System.out.println("SUCESSO: O produto de teste foi salvo e carregado corretamente.");
            } else {
                System.err.println("FALHA: O produto de teste não foi encontrado após carregar os dados.");
            }

        } catch(Exception e) {
            System.err.println("FALHA: Ocorreu um erro no teste de persistência: " + e.getMessage());
        }
    }
}
