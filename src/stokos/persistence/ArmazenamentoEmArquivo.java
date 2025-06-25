// ArmazenamentoEmArquivo.java

package stokos.persistence;


import java.io.*;
import stokos.model.DadosDoSistema;
import stokos.service.ServicoDeArmazenamento;

// Essa classe cuida do armazenamento dos dados do nosso sistema em um arquivo.
// isso é, ela serializa nossos objetos em um arquivo binário.

public class ArmazenamentoEmArquivo implements ServicoDeArmazenamento
{

    private final String caminhoDoArquivo; // final pois não vamos mudar o caminho do arquivo depois de instanciado

    // construtor
    public ArmazenamentoEmArquivo(String caminhoDoArquivo)
    {
        this.caminhoDoArquivo = caminhoDoArquivo;

    }

    @Override
    // salva o estoque em um arquivo binário
    public void salvarDados(DadosDoSistema dados) throws Exception
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.caminhoDoArquivo)))
        {
            oos.writeObject(dados); // serializa o objeto de dados
        }
        catch (Exception e)
        {
            // se ocorrer um erro de IO, vamos lançar uma exceção
            throw new Exception("Erro ao salvar os dados no arquivo: " + e.getMessage());
        }

    }

    @Override
    // carrega o estoque de um arquivo binário
    public DadosDoSistema carregarDados() throws Exception
    {
        File arquivo = new File(caminhoDoArquivo); 

        // verifica se o arquivo de dados já existe
        
        if (arquivo.exists())
        {
            // se ele existe, vamos ler o objeto de dados dentro dele
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo)))
            {
                 return (DadosDoSistema) ois.readObject();
            }
        }

        else // arquivo não existe ou não foi encontrado
        {
            // nesse caso, vamos retornar um novo objeto de dados vazio
            return new DadosDoSistema();
        }
        

    }





}