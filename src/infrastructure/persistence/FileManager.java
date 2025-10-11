package infrastructure.persistence;

import domain.entities.Livro;
import java.util.List;

public interface FileManager {
    void salvarLivros(List<Livro> livros);
    List<Livro> carregarLivros();
}