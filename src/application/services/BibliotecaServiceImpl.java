package application.services;

import domain.entities.Livro;
import domain.exceptions.LivroNaoEncontradoException;
import domain.exceptions.LivroDuplicadoException;
import infrastructure.repositories.BibliotecaRepository;
import presentation.dto.LivroDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BibliotecaServiceImpl implements BibliotecaService {
    private final BibliotecaRepository repository;

    public BibliotecaServiceImpl(BibliotecaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void adicionarLivro(LivroDTO livroDTO) throws LivroDuplicadoException {
        Livro livro = new Livro.Builder()
                .setTitulo(livroDTO.getTitulo())
                .setAutor(livroDTO.getAutor())
                .setAnoPublicacao(livroDTO.getAnoPublicacao())
                .setGenero(livroDTO.getGenero())
                .build();

        repository.adicionarLivro(livro);
    }

    @Override
    public void removerLivro(int id) throws LivroNaoEncontradoException {
        repository.removerLivro(id);
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        return repository.buscarPorTitulo(titulo);
    }

    @Override
    public List<Livro> listarTodosOrdenadosPorAno() {
        return repository.listarOrdenadosPorAno();
    }

    @Override
    public void emprestarLivro(int id) throws LivroNaoEncontradoException {
        Livro livro = repository.buscarPorId(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro com ID " + id + " não encontrado"));

        repository.adicionarNaFilaEmprestimo(livro);
    }

    @Override
    public boolean devolverLivro() {
        return repository.removerDaFilaEmprestimo()
                .map(livro -> {
                    repository.adicionarNoHistoricoDevolvidos(livro);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<Livro> obterFilaEmprestimos() {
        return repository.obterFilaEmprestimos();
    }

    @Override
    public List<Livro> obterHistoricoDevolvidos() {
        return repository.obterHistoricoDevolvidos();
    }

    @Override
    public Map<String, Object> obterEstatisticas() {
        List<Livro> todosLivros = repository.listarTodos();
        Map<String, Object> estatisticas = new HashMap<>();

        estatisticas.put("totalLivros", todosLivros.size());
        estatisticas.put("filaEmprestimos", repository.obterFilaEmprestimos().size());
        estatisticas.put("historicoDevolvidos", repository.obterHistoricoDevolvidos().size());

        Map<String, Long> livrosPorGenero = todosLivros.stream()
                .collect(Collectors.groupingBy(
                        livro -> livro.getGenero().getDescricao(),
                        Collectors.counting()
                ));
        estatisticas.put("livrosPorGenero", livrosPorGenero);

        return estatisticas;
    }
}