/**
 * Bloco de comentario 1
 */
// comentario inline
use std::string::String;

fn funcaoVazia(quem: &str, idade: u32) -> u32 {
    /**
     * Executa uma macro para exibir
     * na tela do usuário Ola Mundo!
     */
}

fn ola(quem: &str) {
    /**
     * Executa uma macro para exibir
     * na tela do usuário Ola Mundo!
     */
    println!("ola {}", quem);
}

fn main() {
    // declaração tipada, atribuição e operador de soma
    let x: u32 = 10 + 5;
    // declaração não tipada, atribuição e chamada de função com parametro
    let word = String::from("mundo");
    // chamada de função com parametro (já via simbolo)
    ola(word);
}
