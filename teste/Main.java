import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // --- PASSO 1: INICIALIZAR OS SERVIÇOS ---
        // Cria o serviço que vai cuidar de salvar e carregar os dados no arquivo "stokos.stk"
        ServicoDeArmazenamento servicoDeArmazenamento = new ArmazenamentoEmArquivo("stokos.stk");
        // Cria o serviço que vai cuidar da lógica de login com os usuários fixos
        ServicoDeAutenticacao servicoDeAutenticacao = new ServicoDeAutenticacao();

        // --- PASSO 2: CARREGAR OS DADOS DO SISTEMA ---
        DadosDoSistema dados;
        try {
            // Tenta carregar os dados do arquivo
            dados = servicoDeArmazenamento.carregarDados();
            System.out.println("Dados do sistema carregados com sucesso.");
        } catch (Exception e) {
            System.out.println("AVISO: Não foi possível carregar os dados. Um novo sistema de dados será criado.");
            e.printStackTrace(); // Imprime o erro detalhado para debug
            dados = new DadosDoSistema(); // Cria um objeto de dados vazio se o carregamento falhar
        }

        // --- PASSO 3: GARANTIR QUE OS OBJETOS PRINCIPAIS EXISTAM ---
        // Se for a primeira vez que o programa roda, o catalogo e o estoque dentro de 'dados' serão null.
        // Precisamos criá-los.
        if (dados.catalogo == null) {
            dados.catalogo = new CatalogoDeProdutos();
        }
        if (dados.estoque == null) {
            // Injeta a dependência do catálogo no estoque, como planejamos!
            dados.estoque = new Estoque(dados.catalogo);
        }

        // --- PASSO 4: INICIAR A INTERFACE GRÁFICA (GUI) ---
        // Finaliza a passagem de dados para uma variável 'final' para ser usada dentro da classe anônima/lambda
        final DadosDoSistema dadosFinais = dados;

        // SwingUtilities.invokeLater é a forma correta e segura de iniciar uma aplicação Swing
        SwingUtilities.invokeLater(() -> {
            // Cria a tela de login, passando os serviços e os dados de que ela precisa
            TelaLogin telaLogin = new TelaLogin(dadosFinais, servicoDeAutenticacao);
            // Torna a tela de login visível para o usuário
            telaLogin.setVisible(true);
        });

        // --- PASSO 5: ADICIONAR UM "GANCHO DE DESLIGAMENTO" PARA SALVAR OS DADOS ---
        // Este é um truque um pouco mais avançado, mas muito útil!
        // Ele garante que, quando o programa for fechado (pelo 'x' da janela),
        // ele tentará salvar os dados antes de terminar.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Desligando... Salvando dados...");
                servicoDeArmazenamento.salvarDados(dadosFinais);
                System.out.println("Dados salvos com sucesso.");
            } catch (Exception e) {
                System.out.println("ERRO CRÍTICO AO SALVAR DADOS NO DESLIGAMENTO:");
                e.printStackTrace();
            }
        }));
    }
}