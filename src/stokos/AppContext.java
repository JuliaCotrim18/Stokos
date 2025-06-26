package stokos;

import stokos.service.*;
import stokos.persistence.*;

import java.io.ObjectInputFilter.Config;

import stokos.model.*;

public class AppContext {
    private static final AppContext instance = new AppContext();

    private ServicoDeArmazenamento servicoDeArmazenamento;
    private ServicoDeAutenticacao servicoDeAutenticacao;
    private DadosDoSistema dados;
    private Usuario usuarioLogado;

    private AppContext() {
        this.servicoDeAutenticacao = new ServicoDeAutenticacao();
        this.servicoDeArmazenamento = new ArmazenamentoEmArquivo(stokos.Config.CAMINHO_ARMAZENAMENTO);
        carregarDados();
    }

    public static AppContext getInstance() {
        return instance;
    }

    public void carregarDados() 
    {
        try {
            this.dados = servicoDeArmazenamento.carregarDados();
            if (this.dados == null) this.dados = new DadosDoSistema(); // Se o arquivo não existe
        } catch (Exception e) {
            System.err.println("Falha ao carregar dados, iniciando com sistema novo.");
            this.dados = new DadosDoSistema();
        }
        
        // Garante que os componentes nunca são nulos após carregar
        if (this.dados.catalogo == null) this.dados.catalogo = new CatalogoDeProdutos();
        if (this.dados.estoque == null) this.dados.estoque = new Estoque(this.dados.catalogo);
        if (this.dados.historicoDeVendas == null) this.dados.historicoDeVendas = new HistoricoDeVendas();
    }

    public void salvarDados() {
        try {
            servicoDeArmazenamento.salvarDados(this.dados);
        } catch (Exception e) {
            System.err.println("Falha crítica ao salvar os dados.");
        }
    }

    // Getters e Setters
    public ServicoDeAutenticacao getServicoDeAutenticacao() { return servicoDeAutenticacao; }
    public DadosDoSistema getDados() { return dados; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuario) { this.usuarioLogado = usuario; }
}