import java.util.ArrayList;


public class ServicoDeAutenticacao
{
    /*
     * Para a funcionalidade de login, 
     * optamos por uma abordagem de protótipo 
     * com usuários fixos para focar o 
     * tempo de desenvolvimento nos 
     * requisitos centrais de Programação Orientada a Objetos 
     * exigidos pela disciplina, como a arquitetura de lotes perecíveis 
     * e não perecíveis e o sistema de 
     * persistência de dados. 
     * A arquitetura, no entanto, está preparada 
     * para a futura implementação de um 
     * gerenciamento de usuários completo.
     */
    private final ArrayList<Usuario> usuarios = new ArrayList<>();

    public ServicoDeAutenticacao()
    {
        // hard-codamos os usuários no construtor 
        // novamente, se tivessemos mais tempo 
        // implementariamos um sistema realista

        usuarios.add(new Usuario("esther", "mc322", Cargo.CEO));
        usuarios.add(new Usuario("athyrson", "mc322", Cargo.ESTAGIARIO));
        usuarios.add(new Usuario("mariana", "mc322", Cargo.ESTAGIARIO));
    }

    public Usuario autenticar(String nomeUsuario, String senha)
    {
        for (Usuario usuario : usuarios)
        {
            if (usuario.getNomeDeUsuario().equals(nomeUsuario) && usuario.getSenha().equals(senha))
            {
                return usuario; // Retorna o usuário autenticado
            }
        }

        return null;
    }
}