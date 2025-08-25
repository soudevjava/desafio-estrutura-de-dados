package factory;

import model.Livro;
import service.IdSequence;

public class LivroFactory {
    private LivroFactory() {}

    public static Livro criarLivro(final int id, final String titulo, final String autor, final int ano, final String genero) {
        return new Livro(id, titulo, autor, ano, genero);
    }

    public static Livro criarLivroAutoId(final String titulo, final String autor, final int ano, final String genero) {
        final var id = IdSequence.getInstance().nextId();
        return new Livro(id, titulo, autor, ano, genero);
    }

}
