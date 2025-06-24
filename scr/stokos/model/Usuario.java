import java.io.Serializable;

public class Usuario implements Serializable
{
    private String nomeDeUsuario;
    private String senha;
    private Cargo cargo;
    private String nome;

    public Usuario(String username, String senha, Cargo cargo)
    {
        this.nomeDeUsuario = username;
        this.senha = senha;
        this.cargo = cargo;
    }

    //Getters
    public String getNomeDeUsuario()
    {
        return nomeDeUsuario;
    }

    public String getSenha()
    {
        return senha;
    }

    public Cargo getCargo()
    {
        return cargo;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getNome()
    {
        if (this.nome.isEmpty())
        {
            return this.nomeDeUsuario; // se não tem nome devolve o do usuário na pior das hipóteses
        }
        return this.nome;
    }
    
}