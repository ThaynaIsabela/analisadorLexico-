package br.dev.tic.analisador;

import java.util.ArrayList;

public class Interpretador {
    private String conteudo;
    private int posicao;
    private ArrayList<Token> tokens;

    Interpretador(String conteudo) {
        this.conteudo = conteudo;
        this.posicao = 0;
        this.tokens = new ArrayList<Token>();
    }

    public void start(ArrayList<String> log) {
        try {
            log.add("Iniciando a interpretação e geração de tokens!");
            Estagio[] estagios = { new ComentarioBloco(), new ComentarioLinha(), new Importacao(), new Funcao() };
            while (this.posicao < this.conteudo.length()) {
                // Enquanto tiver conteudo a ser interpretado executa todos os estagios
                boolean estagioEncontrato = false;
                for (int i = 0; i < estagios.length; i++) {
                    Estagio atual = estagios[i];
                    Resultado resultado = atual.avaliar(this.posicao, this.conteudo);
                    if (resultado.isEncontrado()) {
                        estagioEncontrato = true;
                        this.tokens.addAll(resultado.getTokens());
                        this.posicao = resultado.getFim();
                    }
                }
                if (!estagioEncontrato) {
                    String msgErro = String.format("Não sei o que localizar a partir da posição: %d", this.posicao);
                    throw new Exception(msgErro);
                }
            }
            log.add("Finalizou a geração dos tokens!");
        } catch (Exception err) {
            log.add("Erro ao interpretar o arquivo: " + err.getMessage());
        }
        for (int i = 0; i < this.tokens.size(); i++) {
            this.tokens.get(i).exibirConteudo(0, true, log);
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

}
