package stokos.model;

import java.io.Serializable;

/**
 * A classe `DadosDoSistema` atua como um "container" ou "agregador" para os
 * principais objetos de dados da aplicação. Sua única responsabilidade é agrupar
 * as estruturas de dados centrais (catálogo, estoque, histórico) em um único
 * objeto.
 *
 * CONCEITOS DE POO E DESIGN APLICADOS:
 * - Agregação: Esta classe é um exemplo de agregação, onde ela "tem" referências
 * a outros objetos complexos (`CatalogoDeProdutos`, `Estoque`, `HistoricoDeVendas`).
 * - Data Transfer Object (DTO) / Container: O principal propósito desta classe é
 * facilitar a transferência e o gerenciamento dos dados do sistema como um todo.
 * Ao agrupar tudo em um único objeto, o serviço de persistência (`ArmazenamentoEmArquivo`)
 * precisa apenas serializar e desserializar este objeto, em vez de lidar com
 * cada componente de dados individualmente. Isso simplifica drasticamente o
 * processo de salvar e carregar o estado da aplicação.
 * - Serializable: A implementação da interface `Serializable` é o que torna possível
 * a persistência de objetos desta classe. Quando `DadosDoSistema` é serializado,
 * todos os objetos que ele referencia (e que também são `Serializable`) são
* automaticamente incluídos no processo.
 */
public class DadosDoSistema implements Serializable {

    /**
     * O `serialVersionUID` é um identificador de versão para a classe serializada.
     * Ele é usado durante a desserialização (leitura do arquivo) para verificar
     * se a classe que está carregando o objeto é compatível com a versão da classe
     * que o salvou. Isso ajuda a evitar erros caso a estrutura da classe mude
     * em futuras versões do software.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Referência pública para o catálogo de produtos do sistema.
     * A visibilidade 'public' foi uma decisão de design para simplificar o acesso
     * direto através da instância de `AppContext`. Em uma arquitetura mais complexa,
     * poderíamos usar getters, mas para este contexto, o acesso direto é prático.
     */
    public CatalogoDeProdutos catalogo;

    /**
     * Referência pública para o estoque (conjunto de lotes) do sistema.
     */
    public Estoque estoque;

    /**
     * Referência pública para o histórico de todas as vendas realizadas.
     */
    public HistoricoDeVendas historicoDeVendas;

    /**
     * Construtor da classe DadosDoSistema.
     * Quando um novo conjunto de dados é criado (por exemplo, na primeira vez que
     * a aplicação é executada e não há arquivo de dados para carregar), este
     * construtor garante que as principais estruturas de dados, como o catálogo
     * e o histórico, sejam inicializadas como objetos vazios, mas prontos para uso.
     * Isso evita a ocorrência de `NullPointerException`.
     */
    public DadosDoSistema() {
        this.catalogo = new CatalogoDeProdutos();
        this.historicoDeVendas = new HistoricoDeVendas();
        // Nota: O 'estoque' não é inicializado aqui, pois ele depende do 'catalogo'.
        // Sua inicialização é tratada de forma segura no AppContext após o carregamento
        // dos dados para garantir que a referência ao catálogo seja a correta.
    }
}