package stokos.service;

import stokos.model.DadosDoSistema;

/**
 * A interface `ServicoDeArmazenamento` define um "contrato" para qualquer classe
 * que deseje fornecer um mecanismo de persistência (salvar e carregar) para os
 * dados do sistema.
 *
 * CONCEITO DE DESIGN: PROGRAMAÇÃO VOLTADA A INTERFACES
 * Ao criar esta interface, o resto do sistema (como o `AppContext`) não precisa
 * saber *como* os dados são salvos (em um arquivo, em um banco de dados, na nuvem),
 * mas apenas *que* existe um serviço capaz de executar as ações `salvarDados` e
 * `carregarDados`.
 *
 * Vantagens:
 * - Desacoplamento: A lógica de negócio fica separada da lógica de persistência.
 * - Flexibilidade: Se no futuro quisermos trocar o armazenamento de arquivos por
 * um banco de dados, precisaríamos apenas criar uma nova classe
 * (ex: `ArmazenamentoEmBancoDeDados`) que implementa esta mesma interface,
 * sem a necessidade de alterar o resto do sistema.
 */
public interface ServicoDeArmazenamento {

    /**
     * Define o método para salvar o estado atual dos dados da aplicação.
     * Qualquer classe que implementar esta interface é obrigada a fornecer uma
     * implementação para este método.
     *
     * @param dados O objeto `DadosDoSistema` que contém tudo a ser salvo.
     * @throws Exception Se ocorrer um erro durante o processo de gravação.
     */
    void salvarDados(DadosDoSistema dados) throws Exception;

    /**
     * Define o método para carregar o estado dos dados da aplicação.
     * Qualquer classe que implementar esta interface é obrigada a fornecer uma
     * implementação para este método.
     *
     * @return O objeto `DadosDoSistema` carregado. Se nenhum dado salvo for
     * encontrado, a implementação pode retornar `null` ou um novo objeto
     * de dados vazio.
     * @throws Exception Se ocorrer um erro durante o processo de leitura.
     */
    DadosDoSistema carregarDados() throws Exception;
}