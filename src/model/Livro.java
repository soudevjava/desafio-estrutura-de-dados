package model;

import java.util.Objects;

public class Livro {

    private static int proximoId = 1;

    private int id;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private String genero;

    public Livro(String titulo, String autor, int anoPublicacao, String genero) {
        this.id = proximoId++;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
    }

    public Livro(int id, String titulo, String autor, int anoPublicacao, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public String getGenero() { return genero; }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Título: %-40s | Autor: %-30s | Ano: %-5d | Gênero: %s",
                id, titulo, autor, anoPublicacao, genero);
    }

    public static void atualizarProximoId(int maiorIdConhecido) {
        proximoId = maiorIdConhecido + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(titulo.toLowerCase(), livro.titulo.toLowerCase()) &&
                Objects.equals(autor.toLowerCase(), livro.autor.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                titulo != null ? titulo.toLowerCase() : null,
                autor != null ? autor.toLowerCase() : null
        );
    }
}
