public class Main {

    // Atualmente é para debugar o código, mas será a classe principal do sistema 

    public static void main(String[] args)
    {

        // Cria uma instância do ArmazenamentoEmArquivo
        ServicoDeArmazenamento armazenamento = new ArmazenamentoEmArquivo("estoque.stk");

        DadosDoSistema dadosDoSistema;
        
        try
        {
            dadosDoSistema = armazenamento.carregarDados();
        }
        catch (Exception e)
        {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            // Se não conseguir carregar, cria um novo objeto de DadosDoSistema
            dadosDoSistema = new DadosDoSistema();
        }
        // instância uma classe CatalogoDeProdutos
        CatalogoDeProdutos catalogo = new CatalogoDeProdutos();
        
        


        // instância uma classe Estoque, passando o catálogo de produtos como parâmetro
        Estoque estoque = new Estoque(catalogo);

        // adiciona estoque e catálogo ao DadosDoSistema
        dadosDoSistema.catalogo = catalogo;
        dadosDoSistema.estoque = estoque;

        // Cria um produto e registra ele no catálogo
        Produto produto1 = new Produto(
            "Desodorante Bozzano Invisible", 
            8.90, 
            Grandeza.UNIDADE,
            "7891350032970");

        // Registra o produto no catálogo
        try
        {
            catalogo.cadastrarProduto(produto1);

        }
        catch (ProdutoJaCadastradoException e)
        {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }

        // Adiciona um lote ao estoque
        Lote lote1 = new Lote(produto1, 3);

        try
        {
            estoque.adicionarLote(lote1);
            System.out.println("Lote adicionado com sucesso ao estoque.");
        }
        catch (ProdutoNaoCadastradoException e)
        {
            System.out.println("Erro ao adicionar lote: " + e.getMessage());
        }

        // guarda os dados no arquivo
        try
        {
            armazenamento.salvarDados(dadosDoSistema);
            System.out.println("dados salvo com sucesso.");
        }
        catch (Exception e)
        {
            System.out.println("Erro ao salvar estoque: " + e.getMessage());
        }

        // carrega o estoque do arquivo
        try
        {
            DadosDoSistema dadosCarregado = armazenamento.carregarDados();
            System.out.println("Dados carregado com sucesso.");
        }
        catch (Exception e)
        {
            System.out.println("Erro ao carregar estoque: " + e.getMessage());
        }


    }
    
}
