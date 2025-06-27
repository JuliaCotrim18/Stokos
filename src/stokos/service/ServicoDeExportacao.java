package stokos.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.TableModel;

/**
 * A classe `ServicoDeExportacao` fornece a funcionalidade de exportar dados
 * para um arquivo no formato CSV (Comma-Separated Values).
 *
 * CONCEITO DE DESIGN: CAMADA DE SERVIÇO
 * Esta classe é um bom exemplo de uma camada de serviço. Ela isola uma
 * responsabilidade específica (manipulação de arquivos para exportação) do
 * resto da aplicação, como a interface gráfica (`TelaRelatorios`) e o modelo de dados.
 * Isso torna o código mais organizado, reutilizável e fácil de manter.
 */
public class ServicoDeExportacao {

    /**
     * Exporta os dados de um `TableModel` (o modelo de dados de uma JTable do Swing)
     * para um arquivo CSV.
     *
     * @param model O `TableModel` que contém os dados a serem exportados (cabeçalhos e linhas).
     * @param caminhoDoArquivo O caminho completo do arquivo onde o CSV será salvo.
     * @throws IOException Se ocorrer um erro durante a escrita do arquivo (ex: permissão negada).
     */
    public void exportarParaCSV(TableModel model, String caminhoDoArquivo) throws IOException {
        // BOA PRÁTICA: TRY-WITH-RESOURCES
        // A estrutura `try-with-resources` garante que o `BufferedWriter` seja
        // fechado automaticamente ao final do bloco, mesmo que ocorram exceções.
        // Isso previne vazamentos de recursos (resource leaks).
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoDoArquivo))) {

            // PASSO 1: Escrever o cabeçalho (nomes das colunas)
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i));
                // Adiciona o separador ";" após cada nome de coluna, exceto a última.
                if (i < model.getColumnCount() - 1) {
                    writer.write(";");
                }
            }
            // Pula para a próxima linha do arquivo após escrever o cabeçalho.
            writer.newLine();

            // PASSO 2: Escrever os dados das linhas
            // Loop externo para iterar sobre cada linha da tabela.
            for (int i = 0; i < model.getRowCount(); i++) {
                // Loop interno para iterar sobre cada coluna da linha atual.
                for (int j = 0; j < model.getColumnCount(); j++) {
                    // Escreve o valor da célula no arquivo.
                    writer.write(model.getValueAt(i, j).toString());
                    // Adiciona o separador ";" após cada valor, exceto o último da linha.
                    if (j < model.getColumnCount() - 1) {
                        writer.write(";");
                    }
                }
                // Pula para a próxima linha do arquivo após escrever todos os dados da linha atual.
                writer.newLine();
            }
        }
    }
}