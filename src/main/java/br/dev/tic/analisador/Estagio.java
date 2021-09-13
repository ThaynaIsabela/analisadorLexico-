package br.dev.tic.analisador;

public interface Estagio {
    public String getNomeEstagio();

    public Resultado avaliar(int inicio, String conteudo) throws Exception;
}
