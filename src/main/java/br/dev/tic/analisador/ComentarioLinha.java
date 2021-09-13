package br.dev.tic.analisador;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ComentarioLinha implements Estagio {
    private int numeroComentario;
    private String namespace;

    ComentarioLinha() {
        this.numeroComentario = 0;
        this.namespace = null;
    }

    ComentarioLinha(String namespace) {
        this.namespace = namespace;
    }

    private String getNomeProximoSimbolo() {
        this.numeroComentario += 1;

        return "ComentarioLinha" + (this.namespace != null ? "-" + this.namespace : "")
                + String.format("%d", this.numeroComentario);
    }

    public String getNomeEstagio() {
        return "ComentarioLinha";
    }

    public Resultado avaliar(int inicio, String conteudo) throws Exception {
        // Busca o inicio de onde interpretar (pois o cursor de interpretação vai
        // andando)
        String _conteudo1 = conteudo.substring(inicio);
        Pattern inicioComentario = Pattern.compile("^( |\r|\n)*\\/\\/");
        Matcher resultadoRegex1 = inicioComentario.matcher(_conteudo1);

        if (resultadoRegex1.find()) { // Encontrou um bloco de comentario
            // Calcula a posição relativa no conteudo completo
            int posicaoInicio = resultadoRegex1.start() + inicio;

            // Separa o bloco para achar o fim do comentário
            String _conteudo2 = _conteudo1.substring(resultadoRegex1.end());
            Pattern fimComentario = Pattern.compile("\n|\r|\t");
            Matcher resultadoRegex2 = fimComentario.matcher(_conteudo2);
            if (resultadoRegex2.find()) {
                // Achamos o fim do comentário
                String _conteudoComentario = _conteudo2.substring(0, resultadoRegex2.start());
                int posicaoFim = inicio + resultadoRegex1.end() + resultadoRegex2.end();
                ArrayList<Token> filhos = new ArrayList<Token>();

                Token token = new Token(this.getNomeEstagio(), posicaoInicio, posicaoFim, _conteudoComentario,
                        this.getNomeProximoSimbolo(), new ArrayList<Token>());
                filhos.add(token);
                return new Resultado(posicaoInicio, posicaoFim, conteudo.substring(posicaoInicio, posicaoFim), filhos,
                        true);

            } else {
                throw new Exception("Não foi encontrado o fim do comentário");
            }
        }
        return new Resultado(0, 0, "", new ArrayList<Token>(), false);
    }
}
