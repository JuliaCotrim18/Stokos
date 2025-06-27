package stokos.service;

import java.util.ArrayList;
import stokos.model.Usuario;
import stokos.model.Cargo;

/**
 * A classe `ServicoDeAutenticacao` é responsável pela lógica de verificação
 * de credenciais de usuários (login).
 *
 * CONCEITO DE DESIGN: CAMADA DE SERVIÇO
 * Esta classe isola a lógica de autenticação do resto do sistema. A tela de
 * login (`TelaLogin`) simplesmente coleta os dados e os envia para este serviço,
 * que é o único responsável por validar se o usuário e a senha estão corretos.
 */
public class ServicoDeAutenticacao {

    /**
     * Lista que armazena os usuários do sistema.
     *
     * NOTA DE PROJETO:
     * Para esta versão do sistema, optou-se por uma abordagem de protótipo com
     * usuários fixos ("hard-coded"). Isso permitiu focar o desenvolvimento nas
     * funcionalidades centrais de POO exigidas. A arquitetura, no entanto,
     * está pronta para evoluir para um gerenciamento de usuários dinâmico e
     * persistido no futuro.
     */
    private final ArrayList<Usuario> usuarios = new ArrayList<>();

    /**
     * Construtor do serviço.
     * Popula a lista de usuários com os dados fixos do protótipo.
     */
    public ServicoDeAutenticacao() {
        usuarios.add(new Usuario("esther", "mc322", Cargo.CEO));
        usuarios.add(new Usuario("athyrson", "mc322", Cargo.ESTAGIARIO));
        usuarios.add(new Usuario("mariana", "mc322", Cargo.ESTAGIARIO));
    }

    /**
     * Autentica um usuário com base no nome de usuário e senha fornecidos.
     *
     * @param nomeUsuario O nome de usuário (login) a ser verificado.
     * @param senha A senha a ser verificada.
     * @return O objeto `Usuario` correspondente se a autenticação for bem-sucedida.
     * Retorna `null` se as credenciais forem inválidas.
     */
    public Usuario autenticar(String nomeUsuario, String senha) {
        // Percorre a lista de usuários cadastrados.
        for (Usuario usuario : usuarios) {
            // Compara o nome de usuário e a senha fornecidos com os do usuário atual na lista.
            // A comparação de Strings é feita com o método `.equals()` para checar o conteúdo.
            if (usuario.getNomeDeUsuario().equals(nomeUsuario) && usuario.getSenha().equals(senha)) {
                // Se ambos corresponderem, o usuário é considerado autenticado.
                return usuario; // Retorna o objeto do usuário encontrado.
            }
        }

        // Se o loop terminar sem encontrar uma correspondência, retorna null.
        return null;
    }
}