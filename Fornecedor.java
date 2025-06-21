import java.io.Serializable;

/**
 * Representa um fornecedor de produtos.
 * A classe armazena informações de identificação e contacto do fornecedor.
 * Implementa a interface Serializable para permitir que os seus objetos sejam guardados em ficheiros, se necessário.
 */
public class Fornecedor implements Serializable {

    // Atributos do Fornecedor
    private static int contadorFornecedores = 0;
    private int id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;

    /**
     * Construtor para criar um novo Fornecedor.
     *
     * @param nome O nome do fornecedor (ex: "Distribuidora de Alimentos ABC").
     * @param cnpj O CNPJ (Cadastro Nacional da Pessoa Jurídica) do fornecedor.
     * @param telefone O telefone de contacto do fornecedor.
     * @param email O e-mail de contacto do fornecedor.
     */
    
    public Fornecedor(String nome, String cnpj, String telefone, String email) {
        this.id = ++contadorFornecedores;
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
    }

    public int getId() 
    {
        return id;
    }

    public String getNome() 
    {
        return nome;
    }

    public void setNome(String nome) 
    {
        this.nome = nome;
    }

    public String getCnpj() 
    {
        return cnpj;
    }

    public void setCnpj(String cnpj) 
    {
        this.cnpj = cnpj;
    }

    public String getTelefone() 
    {
        return telefone;
    }

    public void setTelefone(String telefone) 
    {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    /**
     * Retorna uma representação textual do objeto Fornecedor.
     * Facilita a visualização dos dados do fornecedor em relatórios e listagens.
     */
    @Override
    public String toString() {
        return "Fornecedor: " + nome + " (CNPJ: " + cnpj + ")";
    }
}
