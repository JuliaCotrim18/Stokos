package stokos;

/**
 * A classe `Config` é uma classe utilitária que centraliza as constantes
 * e configurações globais da aplicação. O objetivo é ter um único local
 * para gerenciar valores que podem ser usados em várias partes do sistema.
 *
 * CONCEITOS DE DESIGN APLICADOS:
 * - Centralização de Configurações: Evita que valores "mágicos" (como Strings
 * ou números literais) fiquem espalhados pelo código. Se for necessário
 * alterar o caminho de um arquivo, por exemplo, a mudança é feita apenas aqui.
 * - Classe Utilitária com Prevenção de Instanciação: A classe é 'final' para
 * não poder ser herdada, e seu construtor é 'private' para impedir que
 * seja instanciada. Isso reforça que ela serve apenas como um agrupador
 * de constantes estáticas.
 */
public final class Config {

    /**
     * Construtor privado.
     * Impede que um objeto do tipo `Config` seja criado com `new Config()`.
     * Como todos os membros da classe são estáticos, não há necessidade de
     * criar instâncias dela.
     */
    private Config() {}


    /**
     * Define o caminho e o nome do arquivo onde os dados do sistema serão salvos.
     * Usado pelo `ArmazenamentoEmArquivo`.
     */
    public static final String CAMINHO_ARMAZENAMENTO = "./stokos_dados.stk";

    /**
     * Define o caminho e o nome do arquivo de dados de teste.
     * Usado para testes unitários e de integração, permitindo que os testes
     * não interfiram nos dados reais da aplicação.
     */
    public static final String CAMINHO_TESTE_ARMAZENAMENTO = "./stokos_dados_teste.stk";

    /**
     * Define o número de dias de antecedência para um lote ser considerado
     * "próximo do vencimento". Usado na lógica de alertas.
     */
    public static final int DIAS_PARA_ESTAR_PROXIMO_DO_VENCIMENTO = 3;

    /**
     * Define o caminho e o nome do arquivo para exportação de relatórios em CSV.
     * Usado pelo `ServicoDeExportacao`.
     */
    public static final String CAMINHO_SAIDA_ARQUIVO_CSV = "./relatorio_stokos.csv";
}