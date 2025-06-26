package stokos.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.TableModel;

public class ServicoDeExportacao
{
    public void exportarParaCSV(TableModel model, String caminhoDoArquivo) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoDoArquivo)))
        {
            for (int i = 0; i < model.getColumnCount(); i++)
            {
                writer.write(model.getColumnName(i));
                if (i < model.getColumnCount() - 1)
                {
                    writer.write(";");
                }
            }

            writer.newLine();

            for (int i = 0; i < model.getRowCount(); i++)
            {
                for (int j = 0; j < model.getColumnCount(); j++)
                {
                    writer.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1)
                    {
                        writer.write(";");
                    }
                }
                
                writer.newLine();
            }

            
        }
    }
}