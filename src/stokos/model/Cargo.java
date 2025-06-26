package stokos.model;

/**
 * O enum `Cargo` define um conjunto fixo de constantes para representar os
 * diferentes níveis de permissão ou papéis de usuário dentro do sistema.
 *
 * CONCEITOS DE POO E DESIGN APLICADOS:
 * - Enumeração (Enum): O uso de um 'enum' é uma prática de design muito superior
 * a usar constantes de String (ex: "CEO", "Estagiario") ou de inteiros (ex: 1, 2).
 * - Vantagens do Enum:
 * 1. Type Safety (Segurança de Tipo): O compilador garante que uma variável do
 * tipo `Cargo` só pode receber um dos valores definidos (`CEO` ou `ESTAGIARIO`).
 * Isso evita erros de digitação e valores inválidos que poderiam ocorrer
 * com Strings.
 * 2. Legibilidade e Clareza: O código se torna mais claro e expressivo. Uma
 * verificação como `usuario.getCargo() == Cargo.CEO` é muito mais legível
 * do que `usuario.getCargo() == 1`.
 * 3. Manutenibilidade: Se novos cargos precisarem ser adicionados no futuro,
 * basta adicioná-los a este arquivo, e o compilador ajudará a encontrar
 * os locais no código (como 'switch-case') que precisam ser atualizados.
 */
public enum Cargo {
    /**
     * Representa o cargo de mais alto nível (Chief Executive Officer).
     * Usuários com este cargo têm acesso irrestrito a todas as funcionalidades
     * do sistema, como alterar dados de produtos e gerar relatórios financeiros.
     */
    CEO,

    /**
     * Representa um cargo com permissões limitadas.
     * Usuários com este cargo podem ter acesso a funcionalidades operacionais,
     * como registrar entrada e saída de estoque, mas são restringidos de
     * realizar ações administrativas ou estratégicas.
     */
    ESTAGIARIO;
}