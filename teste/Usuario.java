import java.io.Serializable;

public class Usuario implements Serializable
{
    private String nomeDeUsuario;
    private String senha;
    private Cargo cargo;

    public Usuario(String nome, String senha, Cargo cargo)
    {
        this.nomeDeUsuario = nome;
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
}