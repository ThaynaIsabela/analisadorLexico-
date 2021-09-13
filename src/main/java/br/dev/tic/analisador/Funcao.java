package br.dev.tic.analisador;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Funcao implements Estagio {

    public String getNomeEstagio() {
        return "Funcao";
    }

    public Resultado avaliar(int inicio, String conteudo) throws Exception {
        String _conteudo1 = conteudo.substring(inicio);
        Pattern inicioBloco = Pattern.compile(
                "( |\r|\n|\t)*(async )?fn[ \t]+([a-zA-Z0-9-_]+)[ \t]*\\(([a-zA-Z0-9&:_, ]+)?\\)([ \t]*->[ \t]*([a-zA-Z:0-9&]+))?[ \t\n\r]*\\{");
        Pattern fimBloco = Pattern.compile("^( |\n|\r|\t)*}");
        Matcher resultadoRegex1 = inicioBloco.matcher(_conteudo1);

        if (resultadoRegex1.find()) { // Encontrou um bloco
            // Calcula a posição relativa no conteudo completo
            int posicaoInicio = resultadoRegex1.start() + inicio;
            int posicaoAtual = 0;
            String _conteudo2 = _conteudo1.substring(resultadoRegex1.end());
            ArrayList<Token> tokens = new ArrayList<Token>();
            String nomeFuncao = resultadoRegex1.group(3);

            Estagio[] estagios = { new ComentarioBloco(nomeFuncao), new ComentarioLinha(nomeFuncao) };
            while (posicaoAtual < _conteudo2.length()) {
                // Enquanto tiver conteudo a ser interpretado executa todos os estagios
                boolean estagioEncontrato = false;
                for (int i = 0; i < estagios.length; i++) {
                    Estagio atual = estagios[i];
                    Resultado resultado = atual.avaliar(posicaoAtual, _conteudo2);
                    if (resultado.isEncontrado()) {
                        estagioEncontrato = true;
                        tokens.addAll(resultado.getTokens());
                        posicaoAtual = resultado.getFim();
                    }
                }
                Matcher resultadoFimBloco = fimBloco.matcher(_conteudo2.substring(posicaoAtual));
                if (resultadoFimBloco.find()) {
                    // preparo o retorno
                    int posicaoFim = posicaoAtual + resultadoFimBloco.end() + inicio + resultadoRegex1.end();
                    String conteudoFuncao = conteudo.substring(posicaoInicio, posicaoFim);
                    boolean assincrona = resultadoRegex1.group(2) != null;
                    String stringArgumentos = resultadoRegex1.group(4);
                    ArrayList<Argumento> argumentos = new ArrayList<Argumento>();
                    if (stringArgumentos != null) {
                        String[] args = stringArgumentos.split(",");
                        for (int k = 0; k < args.length; k++) {
                            String chaveValor[] = args[k].split(":");
                            argumentos.add(new Argumento(chaveValor[0].trim(), chaveValor[1].trim(), k));
                        }
                    }
                    // Precisa interpretar os argumentos (stringArgumentos);
                    String tipoRetorno = resultadoRegex1.group(6);
                    TokenFuncao tokenFuncao = new TokenFuncao(this.getNomeEstagio(), posicaoInicio, posicaoFim,
                            conteudoFuncao, nomeFuncao, tokens, argumentos, tipoRetorno, assincrona);
                    ArrayList<Token> tokenFuncaoList = new ArrayList<Token>();
                    tokenFuncaoList.add(tokenFuncao);
                    Resultado resultadoFinal = new Resultado(posicaoInicio, posicaoFim, conteudoFuncao, tokenFuncaoList,
                            true);
                    return resultadoFinal;
                } else if (!estagioEncontrato) {
                    String msgErro = String.format("Não sei o que localizar a partir da posição: %d", posicaoAtual);
                    throw new Exception(msgErro);
                }
            }
        }
        return new Resultado(0, 0, "", new ArrayList<Token>(), false);
    }
}
