package infrastructure.repositories;

import domain.entities.Livro;
import domain.exceptions.LivroNaoEncontradoException;
import domain.exceptions.LivroDuplicadoException;
import infrastructure.persistence.FileManager;
import java.util.*;
import java.util.stream.Collectors;

public class BibliotecaRepositoryImpl implements BibliotecaRepository {
    private final List<Livro> livros;
    private final Set<Livro> livrosUnicos;
    private final Map<Integer, Livro> livrosPorId;
    private final Queue<Livro> filaEmprestimos;
    private final Stack<Livro> historicoDevolvidos;
    private final FileManager fileManager;
    private int proximoId;

    public BibliotecaRepositoryImpl(FileManager fileManager) {
        this.livros = new ArrayList<>();
        this.livrosUnicos = new HashSet<>();
        this.livrosPorId = new HashMap<>();
        this.filaEmprestimos = new LinkedList<>();
        this.historicoDevolvidos = new Stack<>();
        this.fileManager = fileManager;
        this.proximoId = 1;
        carregarDados();
    }

    @Override
    public void adicionarLivro(Livro livro) throws LivroDuplicadoException {
        if (livrosUnicos.contains(livro)) {
            throw new LivroDuplicadoException(
                    "Livro já existe: " + livro.getTitulo() + " - " + livro.getAutor());
        }

        Livro novoLivro = new Livro.Builder()
                .setId(proximoId++)
                .setTitulo(livro.getTitulo())
                .setAutor(livro.getAutor())
                .setAnoPublicacao(livro.getAnoPublicacao())
                .setGenero(livro.getGenero())
                .build();

        livros.add(novoLivro);
        livrosUnicos.add(novoLivro);
        livrosPorId.put(novoLivro.getId(), novoLivro);
        salvarDados();
    }

    @Override
    public void removerLivro(int id) throws LivroNaoEncontradoException {
        Livro livro = livrosPorId.get(id);
        if (livro == null) {
            throw new LivroNaoEncontradoException("Livro com ID " + id + " não encontrado");
        }

        livros.remove(livro);
        livrosUnicos.remove(livro);
        livrosPorId.remove(id);
        filaEmprestimos.remove(livro);
        salvarDados();
    }

    @Override
    public Optional<Livro> buscarPorId(int id) {
        return Optional.ofNullable(livrosPorId.get(id));
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        return livros.stream()
                .filter(livro -> livro.getTitulo().toLowerCase()
                        .contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(livros);
    }

    @Override
    public List<Livro> listarOrdenadosPorAno() {
        return livros.stream()
                .sorted(Comparator.comparingInt(Livro::getAnoPublicacao))
                .collect(Collectors.toList());
    }

    @Override
    public void adicionarNaFilaEmprestimo(Livro livro) {
        if (!filaEmprestimos.contains(livro)) {
            filaEmprestimos.offer(livro);
        }
    }

    @Override
    public Optional<Livro> removerDaFilaEmprestimo() {
        return Optional.ofNullable(filaEmprestimos.poll());
    }

    @Override
    public void adicionarNoHistoricoDevolvidos(Livro livro) {
        historicoDevolvidos.push(livro);
    }

    @Override
    public List<Livro> obterFilaEmprestimos() {
        return new ArrayList<>(filaEmprestimos);
    }

    @Override
    public List<Livro> obterHistoricoDevolvidos() {
        List<Livro> historico = new ArrayList<>();
        Stack<Livro> temp = new Stack<>();

        while (!historicoDevolvidos.isEmpty()) {
            Livro livro = historicoDevolvidos.pop();
            historico.add(livro);
            temp.push(livro);
        }

        while (!temp.isEmpty()) {
            historicoDevolvidos.push(temp.pop());
        }

        return historico;
    }

    @Override
    public int obterProximoId() {
        return proximoId;
    }

    @Override
    public boolean existeLivro(Livro livro) {
        return livrosUnicos.contains(livro);
    }

    @Override
    public void salvarDados() {
        fileManager.salvarLivros(livros);
    }

    @Override
    public void carregarDados() {
        List<Livro> livrosCarregados = fileManager.carregarLivros();
        for (Livro livro : livrosCarregados) {
            livros.add(livro);
            livrosUnicos.add(livro);
            livrosPorId.put(livro.getId(), livro);
            if (livro.getId() >= proximoId) {
                proximoId = livro.getId() + 1;
            }
        }
    }
}
