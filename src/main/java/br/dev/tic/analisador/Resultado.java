package br.dev.tic.analisador;

import java.util.ArrayList;

public class Resultado {
    private int inicio;
    private int fim;
    private String conteudo;
    private ArrayList<Token> tokens;
    private boolean encontrado;

    Resultado(int inicio, int fim, String conteudo, ArrayList<Token> tokens, boolean encontrado) {
        this.inicio = inicio;
        this.fim = fim;
        this.conteudo = conteudo;
        this.tokens = tokens;
        this.encontrado = encontrado;
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

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

}
