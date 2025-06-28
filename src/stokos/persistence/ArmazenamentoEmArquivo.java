package stokos.persistence;

import java.io.*;
import stokos.model.DadosDoSistema;
import stokos.service.ServicoDeArmazenamento;

/**
 * A classe `ArmazenamentoEmArquivo` é uma implementação concreta da interface
 * `ServicoDeArmazenamento`. Sua responsabilidade é salvar e carregar o objeto
 * `DadosDoSistema` utilizando a serialização de objetos Java em um arquivo binário.
 *
 * CONCEITO DE DESIGN: SEPARAÇÃO DE RESPONSABILIDADES
 * Esta classe lida exclusivamente com a lógica de I/O (Input/Output) de arquivos.
 * Ela não conhece as regras de negócio, apenas sabe como pegar um objeto de dados
 * e gravá-lo em disco, e vice-versa. Isso a torna uma classe coesa e desacoplada.
 */
public class ArmazenamentoEmArquivo implements ServicoDeArmazenamento {

    // O caminho do arquivo é final, pois não deve ser alterado após a
    // criação do serviço de armazenamento.
    private final String caminhoDoArquivo;

    /**
     * Construtor da classe.
     * Recebe o caminho do arquivo onde os dados serão persistidos.
     * @param caminhoDoArquivo O caminho para o arquivo .stk.
     */
    public ArmazenamentoEmArquivo(String caminhoDoArquivo) {
        this.caminhoDoArquivo = caminhoDoArquivo;
    }

    /**
     * Salva o objeto `DadosDoSistema` em um arquivo binário.
     * Este método implementa a lógica de `salvarDados` definida na interface.
     *
     * @param dados O objeto `DadosDoSistema` a ser serializado e salvo.
     * @throws Exception Se ocorrer um erro durante a escrita do arquivo.
     */
    @Override
    public void salvarDados(DadosDoSistema dados) throws Exception {
        // BOA PRÁTICA: TRY-WITH-RESOURCES
        // A estrutura `try-with-resources` garante que o `ObjectOutputStream`
        // (e os streams que ele encapsula) seja fechado automaticamente no final,
        // prevenindo vazamento de recursos (resource leaks).
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.caminhoDoArquivo))) {
            // O método `writeObject` realiza a serialização: converte o objeto 'dados'
            // e todos os objetos que ele contém em uma sequência de bytes e os
            // escreve no arquivo.
            oos.writeObject(dados);
        } catch (IOException e) {
            // Captura uma exceção de I/O e a relança como uma exceção genérica
            // com uma mensagem mais clara para o contexto da aplicação.
            throw new Exception("Erro ao salvar os dados no arquivo: " + e.getMessage());
        }
    }

    /**
     * Carrega o objeto `DadosDoSistema` a partir de um arquivo binário.
     * Este método implementa a lógica de `carregarDados` definida na interface.
     *
     * @return O objeto `DadosDoSistema` desserializado. Se o arquivo não existir,
     * retorna um novo objeto `DadosDoSistema` vazio.
     * @throws Exception Se ocorrer um erro durante a leitura do arquivo.
     */
    @Override
    public DadosDoSistema carregarDados() throws Exception {
        File arquivo = new File(caminhoDoArquivo);

        // Verifica se o arquivo de dados já existe no caminho especificado.
        if (arquivo.exists()) {
            // Se o arquivo existe, prossegue com a leitura.
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                // O método `readObject` realiza a desserialização: lê a sequência de
                // bytes do arquivo e a reconstrói como um objeto em memória.
                // É necessário fazer um "cast" `(DadosDoSistema)` para que o tipo
                // do objeto retornado seja o correto.
                return (DadosDoSistema) ois.readObject();
            }
        } else {
            // Se o arquivo não for encontrado (ex: primeira vez que o programa é executado),
            // retorna um novo objeto `DadosDoSistema` vazio. Isso garante que a
            // aplicação sempre inicie com um estado válido, em vez de falhar.
            return new DadosDoSistema();
        }
    }
}