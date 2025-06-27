package stokos.model;

import java.io.Serializable;

/**
 * A classe `Usuario` modela uma entidade de usuário no sistema. Ela contém
 * as informações necessárias para autenticação (nome de usuário e senha) e
 * autorização (cargo).
 *
 * CONCEITOS DE POO APLICADOS:
 * - Encapsulamento: Todos os atributos são privados, e o acesso a eles é
 * feito através de métodos públicos (getters e setters). Isso protege
 * a integridade dos dados do usuário (por exemplo, impedindo a alteração
 * direta da senha sem um método apropriado).
 * - Coesão: A classe é coesa, pois todos os seus atributos e métodos estão
 * relacionados à representação de um usuário.
 * - Composição: A classe `Usuario` "tem um" `Cargo`, demonstrando uma
 * relação de composição com o enum `Cargo`.
 * - Serializable: Implementa esta interface para que os dados dos usuários
 * (se fossem salvos dinamicamente) pudessem ser persistidos em arquivo.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributo para o login do usuário (identificador único).
    private String nomeDeUsuario;
    // Atributo para a senha de autenticação.
    private String senha;
    // Atributo que define o nível de permissão do usuário, utilizando o enum Cargo.
    private Cargo cargo;
    // Atributo para o nome completo/real do usuário (opcional).
    private String nome;

    /**
     * Construtor da classe Usuario.
     * Inicializa um novo objeto de usuário com os dados essenciais para o login.
     *
     * @param username O nome de usuário para login.
     * @param senha    A senha para autenticação.
     * @param cargo    O nível de permissão (CEO ou ESTAGIARIO).
     */
    public Usuario(String username, String senha, Cargo cargo) {
        this.nomeDeUsuario = username;
        this.senha = senha;
        this.cargo = cargo;
        // O atributo 'nome' não é inicializado aqui, pois é opcional
        // e pode ser definido posteriormente através do método setNome().
    }

    // --- MÉTODOS GETTERS ---
    // Fornecem acesso (somente leitura) aos dados do usuário.

    /**
     * Retorna o nome de usuário (login).
     * @return o nome de usuário.
     */
    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    /**
     * Retorna a senha do usuário.
     * @return a senha.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Retorna o cargo (nível de permissão) do usuário.
     * @return o objeto Cargo correspondente.
     */
    public Cargo getCargo() {
        return cargo;
    }

    // --- MÉTODOS SETTERS ---

    /**
     * Define ou atualiza o nome completo/real do usuário.
     * @param nome O nome a ser definido.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o nome de exibição do usuário.
     * A lógica foi aprimorada para ser mais robusta: se o nome completo (`nome`)
     * não foi definido (é nulo ou vazio), o método retorna o nome de usuário (`nomeDeUsuario`)
     * como uma alternativa segura.
     *
     * MELHORIA APLICADA:
     * A versão original (`if (this.nome.isEmpty())`) causaria um `NullPointerException`
     * se o atributo `nome` nunca tivesse sido definido (pois seu valor seria `null`).
     * A verificação `this.nome == null || this.nome.trim().isEmpty()` corrige isso,
     * primeiro checando se o nome é nulo e depois se está vazio, tornando o método
     * seguro em todas as situações.
     *
     * @return O nome completo do usuário, ou o nome de login se o primeiro não estiver disponível.
     */
    public String getNome() {
        // Verifica se a string 'nome' é nula ou se está vazia (ignorando espaços em branco).
        if (this.nome == null || this.nome.trim().isEmpty()) {
            // Retorna o nome de usuário como fallback.
            return this.nomeDeUsuario;
        }
        // Se o nome for válido, retorna-o.
        return this.nome;
    }
}