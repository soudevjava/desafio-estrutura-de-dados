package domain.entities;

import domain.enums.Genero;
import java.util.Objects;

public class Livro {
    private final int id;
    private final String titulo;
    private final String autor;
    private final int anoPublicacao;
    private final Genero genero;

    private Livro(Builder builder) {
        this.id = builder.id;
        this.titulo = builder.titulo;
        this.autor = builder.autor;
        this.anoPublicacao = builder.anoPublicacao;
        this.genero = builder.genero;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public Genero getGenero() { return genero; }

    public String toFileFormat() {
        return String.format("%d;%s;%s;%d;%s",
                id, titulo, autor, anoPublicacao, genero.name());
    }

    public static class Builder {
        private int id;
        private String titulo;
        private String autor;
        private int anoPublicacao;
        private Genero genero;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTitulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder setAutor(String autor) {
            this.autor = autor;
            return this;
        }

        public Builder setAnoPublicacao(int anoPublicacao) {
            this.anoPublicacao = anoPublicacao;
            return this;
        }

        public Builder setGenero(Genero genero) {
            this.genero = genero;
            return this;
        }

        public Livro build() {
            validateFields();
            return new Livro(this);
        }

        private void validateFields() {
            if (titulo == null || titulo.trim().isEmpty()) {
                throw new IllegalArgumentException("Título é obrigatório");
            }
            if (autor == null || autor.trim().isEmpty()) {
                throw new IllegalArgumentException("Autor é obrigatório");
            }
            if (anoPublicacao < 1000 || anoPublicacao > 2024) {
                throw new IllegalArgumentException("Ano de publicação inválido");
            }
            if (genero == null) {
                throw new IllegalArgumentException("Gênero é obrigatório");
            }
        }
    }

    public static Livro fromFileFormat(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Formato de arquivo inválido");
        }
        return new Builder()
                .setId(Integer.parseInt(parts[0]))
                .setTitulo(parts[1])
                .setAutor(parts[2])
                .setAnoPublicacao(Integer.parseInt(parts[3]))
                .setGenero(Genero.valueOf(parts[4]))
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return Objects.equals(titulo.toLowerCase(), livro.titulo.toLowerCase()) &&
                Objects.equals(autor.toLowerCase(), livro.autor.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo.toLowerCase(), autor.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s - %s (%d) | Gênero: %s",
                id, titulo, autor, anoPublicacao, genero.getDescricao());
    }
}