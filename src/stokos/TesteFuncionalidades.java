package stokos;

import stokos.model.*;
import stokos.service.ServicoDeAutenticacao;
import stokos.service.ServicoDeExportacao;
import stokos.exception.*;
import javax.swing.table.DefaultTableModel;
import java.io.File; // Importe a classe File
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe dedicada a realizar testes funcionais na lógica de negócio do sistema Stokos.
 * Estes testes são executados via console e não dependem da interface gráfica,
 * permitindo verificar se as regras de negócio estão funcionando corretamente em um ambiente isolado.
 */
public class TesteFuncionalidades {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTES FUNCIONAIS DO STOKOS ---");

        // NOVO: Cria um AppContext ESPECIAL para testes, usando o arquivo de teste.
        // Isso isola completamente os testes da aplicação principal.
        AppContext appTeste = new AppContext(Config.CAMINHO_TESTE_ARMAZENAMENTO);

        // NOVO: Usar um bloco try-finally para garantir a limpeza no final.
        try {
            // Executar a sequência de testes, sempre limpando o estado antes de cada um.
            
            System.out.println("\n[1. TESTE: AUTENTICAÇÃO DE USUÁRIOS]");
            limparDados(appTeste);
            testeAutenticacaoDeUsuarios(appTeste.getServicoDeAutenticacao());
            
            System.out.println("\n[2. TESTE: CADASTRO DE PRODUTOS]");
            limparDados(appTeste);
            testeCadastroDeProdutos(appTeste.getDados().catalogo);
            
            System.out.println("\n[3. TESTE: ADIÇÃO DE LOTES AO ESTOQUE]");
            limparDados(appTeste);
            testeAdicaoDeLotes(appTeste);
            
            System.out.println("\n[4. TESTE: REGISTRO DE VENDA]");
            limparDados(appTeste);
            testeRegistroDeVenda(appTeste);
            
            System.out.println("\n[5. TESTE: GERAÇÃO DE ALERTAS DE ESTOQUE]");
            limparDados(appTeste);
            testeAlertasDeEstoque(appTeste);
            
            System.out.println("\n[6. TESTE: MODIFICAÇÃO DE PRODUTO]");
            limparDados(appTeste);
            testeModificacaoDeProduto(appTeste.getDados().catalogo);

            System.out.println("\n[7. TESTE: EXPORTAÇÃO PARA CSV]");
            limparDados(appTeste);
            testeExportacaoCSV();
            
            System.out.println("\n[8. TESTE: PERSISTÊNCIA DE DADOS (SALVAR/CARREGAR)]");
            limparDados(appTeste);
            testePersistencia(appTeste);

        } finally {
            // PASSO FINAL E CRUCIAL: Limpar o arquivo de dados de teste.
            // Este bloco é executado mesmo que um teste falhe.
            File arquivoDeTeste = new File(Config.CAMINHO_TESTE_ARMAZENAMENTO);
            if (arquivoDeTeste.exists()) {
                if (arquivoDeTeste.delete()) {
                    System.out.println("\n-> Arquivo de dados de teste foi limpo com sucesso.");
                } else {
                    System.err.println("\n-> Falha ao limpar o arquivo de dados de teste.");
                }
            }
        }

        System.out.println("\n--- FIM DOS TESTES ---");
    }

    /**
     * Limpa completamente os dados do contexto de teste para garantir
     * que cada teste comece a partir de um estado conhecido e zerado.
     */
    public static void limparDados(AppContext app) {
        // Reinicializa completamente os dados, garantindo um estado limpo.
        app.getDados().catalogo = new CatalogoDeProdutos();
        app.getDados().estoque = new Estoque(app.getDados().catalogo);
        app.getDados().historicoDeVendas = new HistoricoDeVendas();
        
        // Reseta os contadores estáticos para que a contagem de IDs comece do zero.
        Lote.setContadorLotes(0);
        Produto.setContadorProdutos(0);
        System.out.println("-> Ambiente de teste zerado para o próximo teste.");
    }

    /**
     * Testa o serviço de autenticação com um login válido, um inválido e um com senha errada.
     */
    public static void testeAutenticacaoDeUsuarios(ServicoDeAutenticacao servico) {
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
     * O teste agora é autossuficiente: ele primeiro cadastra o produto necessário.
     */
    public static void testeAdicaoDeLotes(AppContext app) {
        try {
            // Setup do teste
            Produto detergente = new ProdutoComum("789001", "Detergente Ypê", 2.50, Grandeza.UNIDADE);
            app.getDados().catalogo.cadastrarProduto(detergente);

            // Ação de teste
            Lote lote1 = new LoteNaoPerecivel(detergente, 100);
            app.getDados().estoque.adicionarLote(lote1);

            // Verificação
            if (app.getDados().estoque.getQuantidadeDisponivel("789001") == 100) {
                System.out.println("SUCESSO: Lote de 100 unidades de 'Detergente' adicionado.");
            } else {
                System.err.println("FALHA: A quantidade em estoque não reflete a adição do lote.");
            }

        } catch (Exception e) {
            System.err.println("ERRO INESPERADO NO TESTE: " + e.getMessage());
        }
    }

    /**
     * Testa o registro de uma venda.
     * O teste agora é autossuficiente.
     */
    public static void testeRegistroDeVenda(AppContext app) {
        try {
            // Setup do teste
            Produto detergente = new ProdutoComum("789001", "Detergente Ypê", 2.50, Grandeza.UNIDADE);
            app.getDados().catalogo.cadastrarProduto(detergente);
            Lote lote1 = new LoteNaoPerecivel(detergente, 100);
            app.getDados().estoque.adicionarLote(lote1);

            Estoque estoque = app.getDados().estoque;
            HistoricoDeVendas historico = app.getDados().historicoDeVendas;

            // Teste de sucesso
            System.out.println("-> Estoque de Detergente ANTES da venda: " + estoque.getQuantidadeDisponivel("789001"));
            estoque.registrarVenda("789001", 10, historico);
            System.out.println("SUCESSO: Venda de 10 unidades de 'Detergente' registrada.");
            if(estoque.getQuantidadeDisponivel("789001") == 90){
                System.out.println("-> Estoque de Detergente DEPOIS da venda: " + estoque.getQuantidadeDisponivel("789001"));
            } else {
                 System.err.println("FALHA: O estoque não foi atualizado corretamente após a venda.");
            }
            
            // Teste de falha
            System.out.println("\n-> Testando falha esperada (quantidade insuficiente)...");
            estoque.registrarVenda("789001", 1000, historico);

        } catch (QuantidadeInsuficienteException e) {
            System.out.println("SUCESSO NO TESTE DE FALHA: Exceção de quantidade insuficiente capturada como esperado.");
        } catch (Exception e) {
            System.err.println("ERRO INESPERADO NO TESTE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Testa a lógica de geração de alertas de estoque baixo e de vencimento próximo.
     */
    public static void testeAlertasDeEstoque(AppContext app) {
        try {
            // Setup para o teste
            Produto iogurte = new ProdutoComum("789003", "Iogurte Natural", 4.00, Grandeza.UNIDADE);
            iogurte.setEstoqueMinimo(10); // Estoque mínimo de 10 unidades
            app.getDados().catalogo.cadastrarProduto(iogurte);
            
            // Lote com quantidade abaixo do mínimo E perto de vencer
            Lote loteIogurte = new LotePerecivel(iogurte, 5, java.time.LocalDate.now().plusDays(2)); // Vence em 2 dias
            app.getDados().estoque.adicionarLote(loteIogurte);

            // Lógica de geração de alertas (simulando a da TelaAvisos)
            ArrayList<String> alertas = new ArrayList<>();
            // Verificação de estoque mínimo
            for (Produto p : app.getDados().catalogo.getListaDeProdutos()) {
                double qtdDisponivel = app.getDados().estoque.getQuantidadeDisponivel(p.getCodigoDeBarras());
                if (p.getEstoqueMinimo() > 0 && qtdDisponivel <= p.getEstoqueMinimo()) {
                    alertas.add("ESTOQUE BAIXO: " + p.getNomeDoProduto());
                }
            }
            // Verificação de vencimento
            for (Lote l : app.getDados().estoque.getLotes()) {
                if (l.estaPertoDeVencer()) {
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
        // Setup
        try {
            Produto pOriginal = new ProdutoComum("789001", "Detergente Ypê", 2.50, Grandeza.UNIDADE);
            catalogo.cadastrarProduto(pOriginal);

            // Ação de teste
            Produto pParaModificar = catalogo.buscarProduto("789001");
            pParaModificar.setNomeDoProduto("Detergente Ypê SUPER");
            pParaModificar.setPrecoUnitario(3.00);

            // Verificação
            Produto pModificado = catalogo.buscarProduto("789001");
            if (pModificado.getNomeDoProduto().equals("Detergente Ypê SUPER") && pModificado.getPrecoUnitario() == 3.00) {
                System.out.println("SUCESSO: Produto foi alterado com sucesso.");
            } else {
                System.err.println("FALHA: A modificação do produto não foi refletida.");
            }
        } catch (Exception e) {
             System.err.println("FALHA: Produto para modificação não encontrado ou erro inesperado.");
        }
    }
    
    /**
     * Testa a exportação de dados para um arquivo CSV e verifica seu conteúdo.
     */
    public static void testeExportacaoCSV() {
        String caminhoTeste = "./relatorio_teste.csv";
        ServicoDeExportacao servico = new ServicoDeExportacao();
        
        // 1. Criar um modelo de tabela com dados de teste
        String[] colunas = {"Produto", "Preço"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        model.addRow(new Object[]{"Feijão", "10.0"});
        model.addRow(new Object[]{"Arroz", "20.5"});

        try {
            // 2. Exportar
            servico.exportarParaCSV(model, caminhoTeste);
            
            // 3. Ler e verificar o arquivo gerado
            try (BufferedReader reader = new BufferedReader(new FileReader(caminhoTeste))) {
                String header = reader.readLine();
                String row1 = reader.readLine();
                String row2 = reader.readLine();

                if ("Produto;Preco".equals(header) && "Feijão;10.0".equals(row1) && "Arroz;20.5".equals(row2)) {
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
     * O teste de persistência agora é seguro e não afeta a aplicação.
     */
    public static void testePersistencia(AppContext app) {
        try {
            // 1. Adicionar um produto único para o teste
            Produto produtoTeste = new ProdutoComum("999999", "Produto de Teste Persistencia", 99.99, Grandeza.UNIDADE);
            app.getDados().catalogo.cadastrarProduto(produtoTeste);

            // 2. Salvar os dados no arquivo de TESTE
            app.salvarDados();
            System.out.println("-> Dados salvos no arquivo de teste.");

            // 3. Simular reinicialização: Criar um NOVO AppContext de teste
            // que forçará o carregamento a partir do arquivo que acabamos de salvar.
            System.out.println("-> Simulando reinicialização do sistema...");
            AppContext appRecarregado = new AppContext(Config.CAMINHO_TESTE_ARMAZENAMENTO);
            System.out.println("-> Dados carregados do arquivo de teste.");

            // 4. Verificar se o produto de teste foi carregado
            Produto produtoCarregado = appRecarregado.getDados().catalogo.buscarProduto("999999");
            if (produtoCarregado != null && produtoCarregado.getNomeDoProduto().equals("Produto de Teste Persistencia")) {
                System.out.println("SUCESSO: O produto de teste foi salvo e carregado corretamente.");
            } else {
                System.err.println("FALHA: O produto de teste não foi encontrado após carregar os dados.");
            }

        } catch(Exception e) {
            System.err.println("FALHA: Ocorreu um erro no teste de persistência: " + e.getMessage());
            e.printStackTrace();
        }
    }
}