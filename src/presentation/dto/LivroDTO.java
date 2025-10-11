package presentation.dto;

import domain.enums.Genero;

public class LivroDTO {
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private Genero genero;

    public LivroDTO(String titulo, String autor, int anoPublicacao, Genero genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public Genero getGenero() { return genero; }
}