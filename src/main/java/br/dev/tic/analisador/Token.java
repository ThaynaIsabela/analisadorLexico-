package br.dev.tic.analisador;

import java.util.ArrayList;

public class Token {
    protected String tipo;
    protected ArrayList<Token> filhos;
    protected String conteudo;
    protected String simbolo;
    protected int inicio;
    protected int fim;

    Token() {
        this.inicio = 0;
        this.fim = 0;
        this.conteudo = "";
        this.simbolo = "";
        this.filhos = new ArrayList<Token>();

    }

    Token(String tipo, int inicio, int fim, String conteudo, String simbolo, ArrayList<Token> filhos) {
        this.tipo = tipo;
        this.inicio = inicio;
        this.fim = fim;
        this.conteudo = conteudo;
        this.simbolo = simbolo;
        this.filhos = filhos;
    }

    public void exibirConteudo(int identacao, boolean exibirConteudo, ArrayList<String> log) {
        String espacos = new String(new char[identacao]).replace("\0", " ");
        log.add(espacos + "Token - Tipo: " + this.tipo + " - Simbolo: " + this.simbolo);
        if (exibirConteudo) {
            log.add(String.format("%s [[ inicio: %d - fim: %d ]]", espacos, this.inicio, this.fim));
            String[] conteudo = this.conteudo.split("\n");
            String reformatada = espacos + " >> " + String.join(espacos + "\n" + espacos + " >> ", conteudo);
            log.add(reformatada);
        }
        for (int i = 0; i < this.filhos.size(); i++) {
            Token meuFilho = this.filhos.get(i);
            meuFilho.exibirConteudo(identacao + 4, exibirConteudo, log);
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Token> getFilhos() {
        return filhos;
    }

    public void setFilhos(ArrayList<Token> filhos) {
        this.filhos = filhos;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFim() {
        return fim;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

}
