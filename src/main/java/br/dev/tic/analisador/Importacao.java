package br.dev.tic.analisador;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Importacao implements Estagio {
    private int numero;

    Importacao() {
        this.numero = 0;
    }

    private String getNomeProximoSimbolo() {
        this.numero += 1;
        return "Importacao" + String.format("%d", this.numero);
    }

    public String getNomeEstagio() {
        return "Importacao";
    }

    public Resultado avaliar(int inicio, String conteudo) throws Exception {
        // Busca o inicio de onde interpretar (pois o cursor de interpretação vai
        // andando)
        String _conteudo1 = conteudo.substring(inicio);
        Pattern inicioBloco = Pattern.compile("^( |\r|\n|\t)*use ");
        Matcher resultadoRegex1 = inicioBloco.matcher(_conteudo1);

        if (resultadoRegex1.find()) { // Encontrou um bloco
            // Calcula a posição relativa no conteudo completo
            int posicaoInicio = resultadoRegex1.start() + inicio;

            // Separa o bloco para achar o fim do bloco
            String _conteudo2 = _conteudo1.substring(resultadoRegex1.end());
            Pattern fimBloco = Pattern.compile(";");
            Matcher resultadoRegex2 = fimBloco.matcher(_conteudo2);
            if (resultadoRegex2.find()) {
                // Achamos o fim do bloco
                String _conteudoBloco = _conteudo2.substring(0, resultadoRegex2.start());
                int posicaoFim = inicio + resultadoRegex1.end() + resultadoRegex2.end();
                ArrayList<Token> filhos = new ArrayList<Token>();

                Token token = new Token(this.getNomeEstagio(), posicaoInicio, posicaoFim, _conteudoBloco,
                        this.getNomeProximoSimbolo(), new ArrayList<Token>());
                filhos.add(token);
                return new Resultado(posicaoInicio, posicaoFim, conteudo.substring(posicaoInicio, posicaoFim), filhos,
                        true);

            } else {
                throw new Exception("Não foi encontrado o fim da importação");
            }
        }
        return new Resultado(0, 0, "", new ArrayList<Token>(), false);
    }
}
