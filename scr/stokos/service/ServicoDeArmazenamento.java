// ServicoDeArmazenamento.java

/**
 * Define o contrato para qualquer classe que queira
 * salvar e carregar o estado do estoque do sistema.
 * no nosso caso, temos apenas a opção de armazenar em um arquivo
 * mas no futuro podemos ter outras opções, como banco de dados, por isso a interface.
 */

package stokos.service;

import stokos.model.DadosDoSistema;

public interface ServicoDeArmazenamento {

    /**
     * Salva o estado atual do estoque.
     * @param estoque O objeto Estoque a ser salvo.
     * @throws Exception Se ocorrer um erro durante a gravação.
     */
    void salvarDados(DadosDoSistema dados) throws Exception; // todo serviço de armazenamento deve salvar nosso estoque

    /**
     * Carrega o estado do estoque.
     * @return O objeto Estoque carregado. Se não houver estado salvo,
     * pode retornar um novo estoque vazio.
     * @throws Exception Se ocorrer um erro durante a leitura.
     */
    DadosDoSistema carregarDados() throws Exception; // e também deve carregar o DadosDoSistema salvo
}