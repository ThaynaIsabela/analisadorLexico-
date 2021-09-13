package br.dev.tic.analisador;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.nio.file.Files;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class UI extends JFrame {

    UI() {
        super("Analisador de código rust");
        BorderLayout blayout = new BorderLayout();
        this.setLayout(blayout);

        final JFileChooser dialogoAbrir = new JFileChooser();

        // Barra de Menus
        JMenuBar menuBar = new JMenuBar();
        JMenu arquivo = new JMenu("Arquivo");
        JMenuItem arquivoAbrir = new JMenu("Abrir");
        JMenuItem arquivoSalvar = new JMenu("Salvar");
        JMenuItem arquivoFechar = new JMenu("Sair");
        JMenu compilador = new JMenu("Compilador");
        arquivo.add(arquivoAbrir);
        arquivo.add(arquivoSalvar);
        arquivo.addSeparator();
        arquivo.add(arquivoFechar);
        menuBar.add(arquivo);
        menuBar.add(compilador);
        this.setJMenuBar(menuBar);

        // Toolbar
        JPanel toolbar = new JPanel();
        JLabel nomeCodigo = new JLabel("<undefined.rs>");
        JButton botaoAbrir = new JButton("Abrir");
        JButton botaoExecutar = new JButton("Executar");
        toolbar.setLayout(new FlowLayout());
        toolbar.add(nomeCodigo);
        toolbar.add(botaoAbrir);
        toolbar.add(botaoExecutar);

        // Area de código
        JPanel areaCodigo = new JPanel();
        BorderLayout areaCodigoLayout = new BorderLayout();
        JTextArea editorCodigo = new JTextArea();
        JScrollPane scrollEditorCodigo = new JScrollPane(editorCodigo);
        editorCodigo.setFont(new Font("monospaced", Font.PLAIN, 12));
        areaCodigo.setLayout(areaCodigoLayout);
        areaCodigo.add(toolbar, BorderLayout.NORTH);
        areaCodigo.add(scrollEditorCodigo, BorderLayout.CENTER);

        this.add(areaCodigo, BorderLayout.CENTER);
        this.setSize(900, 800);

        // Area de Lista dos Tokens
        JPanel lateral = new JPanel();
        lateral.setPreferredSize(new Dimension(200, 200));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("AST Código");
        JTree tree = new JTree(top);
        JScrollPane treeView = new JScrollPane(tree);
        lateral.setLayout(new BorderLayout());
        lateral.add(treeView, BorderLayout.CENTER);
        this.add(lateral, BorderLayout.WEST);

        // Area de console log
        JPanel console = new JPanel();
        JTextArea logger = new JTextArea();
        JScrollPane scrollLogger = new JScrollPane(logger);
        logger.setFont(new Font("monospaced", Font.PLAIN, 12));
        console.setPreferredSize(new Dimension(200, 200));
        console.setMinimumSize(new Dimension(200, 200));
        console.setLayout(new BorderLayout());
        console.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
        console.add(scrollLogger, BorderLayout.CENTER);
        this.add(console, BorderLayout.SOUTH);

        botaoAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogoAbrir.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int returnValue = dialogoAbrir.showOpenDialog(botaoAbrir.getParent());
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File f = dialogoAbrir.getSelectedFile();
                    String conteudo = lerConteudoArquivo(f.getAbsolutePath());
                    nomeCodigo.setText(f.getAbsolutePath());
                    editorCodigo.setText(conteudo);
                }
            }
        });

        botaoExecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> log = new ArrayList<String>();
                Interpretador inter = new Interpretador(editorCodigo.getText());
                inter.start(log);
                logger.setText(String.join("\n", log));
                top.removeAllChildren();
                for (int x = 0; x < inter.getTokens().size(); x++) {
                    Token tk = inter.getTokens().get(x);
                    DefaultMutableTreeNode item = createNodes(top, tk);
                }
            }
        });
    }

    public DefaultMutableTreeNode createNodes(DefaultMutableTreeNode top, Token tk) {
        String titulo = String.format("Tipo: %s, Simbolo: %s", tk.getTipo(), tk.getSimbolo());
        DefaultMutableTreeNode item = new DefaultMutableTreeNode(titulo);
        top.add(item);

        for (int x = 0; x < tk.getFilhos().size(); x++) {
            Token filho = tk.getFilhos().get(x);
            String tituloFilho = String.format("Tipo: %s, Simbolo: %s", filho.getTipo(), filho.getSimbolo());
            DefaultMutableTreeNode itemFilho = new DefaultMutableTreeNode(tituloFilho);
            item.add(itemFilho);
            createNodes(itemFilho, filho);
        }
        return item;
    }

    public String lerConteudoArquivo(String fileName) {
        try {
            return Files.readString(Paths.get(fileName));
        } catch (Exception ex) {
            System.err.println("lerConteudoArquivo(): " + ex.getMessage());
        }
        return "";
    }

    public void start() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); // CONFIGURA A VISIBILIDADE
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.start();
    }

}
