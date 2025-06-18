import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto 
{
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String nomeDoProduto, double precoUnitario, Grandeza grandeza, Fornecedor fornecedor, LocalDate dataDeValidade) 
    {
        super(nomeDoProduto, precoUnitario, grandeza, fornecedor); // Chama o construtor da classe mãe
        this.dataDeValidade = dataDeValidade;
    }

    // Construtor sobrecarregado : se o fornecedor não for necessário, podemos ter um construtor que não o recebe
    public ProdutoPerecivel(String nomeDoProduto, double precoUnitario, Grandeza grandeza, LocalDate dataDeValidade) 
    {
        super(nomeDoProduto, precoUnitario, grandeza); // Chama o construtor da classe mãe
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
    public String getDetalhes() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Data de Validade: " + this.dataDeValidade.format(formatador);
    }

}


