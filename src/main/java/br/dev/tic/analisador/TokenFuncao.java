package br.dev.tic.analisador;

import java.util.ArrayList;

class Argumento {
    private String nome;
    private String tipo;
    private int ordem;

    Argumento(String nome, String tipo, int ordem) {
        this.nome = nome;
        this.tipo = tipo;
        this.ordem = ordem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String toString() {
        return String.format("#%d: Arg: %s - Tipo: %s", this.ordem, this.nome, this.tipo);
    }
}

public class TokenFuncao extends Token {
    private ArrayList<Argumento> argumentos;
    private String tipoRetorno;
    private boolean assincrona;

    TokenFuncao() {
        super();
        this.assincrona = false;
        this.tipoRetorno = null;
        this.argumentos = new ArrayList<Argumento>();
    }

    TokenFuncao(String tipo, int inicio, int fim, String conteudo, String simbolo, ArrayList<Token> filhos) {
        super(tipo, inicio, fim, conteudo, simbolo, filhos);
        this.assincrona = false;
        this.tipoRetorno = null;
        this.argumentos = new ArrayList<Argumento>();
    }

    TokenFuncao(String tipo, int inicio, int fim, String conteudo, String simbolo, ArrayList<Token> filhos,
            ArrayList<Argumento> argumentos, String tipoRetorno, boolean assincrona) {
        super(tipo, inicio, fim, conteudo, simbolo, filhos);
        this.assincrona = assincrona;
        this.tipoRetorno = tipoRetorno;
        this.argumentos = argumentos;
    }

    public void exibirConteudo(int identacao, boolean exibirConteudo, ArrayList<String> log) {
        String espacos = new String(new char[identacao]).replace("\0", " ");
        log.add(espacos + "Token - Tipo: " + this.tipo + " - Simbolo: " + this.simbolo);
        log.add(espacos + " --> Assincrona: " + (this.assincrona ? "sim" : "não"));
        if (this.argumentos.size() > 0) {
            log.add(espacos + " --> Argumentos: ");
            String argumentos = "";
            for (int x = 0; x < this.argumentos.size(); x++) {
                argumentos += espacos + "     --> " + this.argumentos.get(x).toString();
                if (x < this.argumentos.size() - 1)
                    argumentos += "\n";
            }
            log.add(argumentos);
        }
        if (this.tipoRetorno != null)
            log.add(espacos + " --> Retorno: " + this.tipoRetorno.toString());
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

}
