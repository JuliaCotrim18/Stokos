import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto 
{
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String nomeDoProduto, double precoUnitario, Grandeza grandeza, Fornecedor fornecedor, LocalDate dataDeValidade) 
    {
        super(nomeDoProduto, precoUnitario, grandeza, fornecedor); // Chama o construtor da superclasse
        this.dataDeValidade = dataDeValidade;
    }

    public LocalDate getDataDeValidade()
    {
        return this.dataDeValidade;
    }

    public int getDiasParaVencimento()
    {
        return LocalDate.now().until(this.dataDeValidade).getDays();
    } 

    @Override
    public String toString() 
    {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() + "| Data de Validade: " + this.dataDeValidade.format(formatador);
    }

}


