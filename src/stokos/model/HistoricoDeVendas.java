package stokos.model;

import java.io.Serializable;
import java.util.ArrayList;
// A importação que faltava
import stokos.model.RegistroDeVenda; 

/**
 * Armazena a lista de todas as vendas realizadas no sistema.
 */
public class HistoricoDeVendas implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<RegistroDeVenda> registros;

    public HistoricoDeVendas() {
        this.registros = new ArrayList<>();
    }

    public void adicionarRegistro(RegistroDeVenda registro) {
        this.registros.add(registro);
    }
    
    public double getLucroTotalPorProduto(String codigoDeBarras) {
        double lucroTotal = 0.0;
        for (RegistroDeVenda registro : registros) { // A variável 'registros' deve ser o nome da lista
            if (registro.getCodigoDeBarrasProduto().equals(codigoDeBarras)) {
                lucroTotal += registro.getLucroDaVenda();
            }
        }
        return lucroTotal;
    }
    
    public double getQuantidadeTotalVendida(String codigoDeBarras) {
        double quantidadeTotal = 0.0; // Corrigido o nome da variável (minúscula)
        for (RegistroDeVenda registro : registros) { // A variável 'registros' deve ser o nome da lista
            if (registro.getCodigoDeBarrasProduto().equals(codigoDeBarras)) {
                quantidadeTotal += registro.getQuantidadeVendida();
            }
        }
        return quantidadeTotal; // Retorna a variável correta
    }
}