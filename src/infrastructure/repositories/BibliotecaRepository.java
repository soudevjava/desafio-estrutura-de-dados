package infrastructure.repositories;

import domain.entities.Livro;
import domain.exceptions.LivroNaoEncontradoException;
import domain.exceptions.LivroDuplicadoException;
import java.util.List;
import java.util.Optional;

public interface BibliotecaRepository {
    void adicionarLivro(Livro livro) throws LivroDuplicadoException;
    void removerLivro(int id) throws LivroNaoEncontradoException;
    Optional<Livro> buscarPorId(int id);
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> listarTodos();
    List<Livro> listarOrdenadosPorAno();
    void adicionarNaFilaEmprestimo(Livro livro);
    Optional<Livro> removerDaFilaEmprestimo();
    void adicionarNoHistoricoDevolvidos(Livro livro);
    List<Livro> obterFilaEmprestimos();
    List<Livro> obterHistoricoDevolvidos();
    int obterProximoId();
    boolean existeLivro(Livro livro);
    void salvarDados();
    void carregarDados();
}