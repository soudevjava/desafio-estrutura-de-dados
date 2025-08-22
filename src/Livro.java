import java.util.Objects;

public class Livro {

    private final int id;
    private final String titulo;
    private final String autor;
    private final int anoPublicacao;
    private final String genero;


    public Livro(int id, String titulo, String autor, int anoPublicacao, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
    }


    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getGenero() {
        return genero;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return id == livro.id && anoPublicacao == livro.anoPublicacao && Objects.equals(titulo, livro.titulo) && Objects.equals(autor, livro.autor) && Objects.equals(genero, livro.genero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, autor, anoPublicacao, genero);
    }

    @Override
    public String toString() {
        return "ID= " + id +
                ", Titulo= '" + titulo + '\'' +
                ", Autor=' " + autor + '\'' +
                ", Ano de Publicacao= " + anoPublicacao +
                ", Genero='" + genero + '\'' +
                '}';
    }
}
