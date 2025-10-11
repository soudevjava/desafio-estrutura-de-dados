package application.services;

import domain.entities.Livro;
import domain.exceptions.LivroNaoEncontradoException;
import domain.exceptions.LivroDuplicadoException;
import presentation.dto.LivroDTO;
import java.util.List;
import java.util.Map;

public interface BibliotecaService {
    void adicionarLivro(LivroDTO livroDTO) throws LivroDuplicadoException;
    void removerLivro(int id) throws LivroNaoEncontradoException;
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> listarTodosOrdenadosPorAno();
    void emprestarLivro(int id) throws LivroNaoEncontradoException;
    boolean devolverLivro();
    List<Livro> obterFilaEmprestimos();
    List<Livro> obterHistoricoDevolvidos();
    Map<String, Object> obterEstatisticas();
}