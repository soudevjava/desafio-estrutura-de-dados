package service;


import exceptions.BuscaInvalidaException;
import exceptions.DuplicateException;
import exceptions.NotFoundException;
import model.Livro;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Biblioteca {
    private final List<Livro> lista = new ArrayList<>();
    private final Set<Livro> set = new HashSet<>();
    private final Map<Integer, Livro> map = new HashMap<>();
    private final Queue<Livro> filaEmprestimos = new LinkedList<>();
    private final Stack<Livro> historicoDevolucoes = new Stack<>();
    private int ultimoId = 0;

    // -------------------- Util --------------------
    private static String sanitizeTitulo(String titulo) {
        return titulo
                .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }

    private static String hoje() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    private static void validarTermo(String termo) {
        if (termo == null || termo.trim().length() < 3)
            throw new BuscaInvalidaException("Termo de busca deve ter ao menos 3 caracteres.");
    }

    // -------------------- Persistência por livro (TXT/CSV) --------------------
    /**
     * Salva 1 livro em arquivo TXT/CSV, evitando duplicidade por título (independente da data).
     * Pastas: src/main/resources/livros-txt/ | src/main/resources/livros-csv/
     * Nome: dd-MM-yyyy-Titulo-Sanitizado.ext
     */
    public void salvarLivroEmArquivo(Livro livro, String tipo) {
        final boolean txt = "txt".equalsIgnoreCase(tipo);
        final String pasta = txt ? "src/main/resources/livros-txt/" : "src/main/resources/livros-csv/";
        final String extensao = txt ? ".txt" : ".csv";

        try {
            Files.createDirectories(Paths.get(pasta));
            final String tituloSan = sanitizeTitulo(livro.getTitulo());
            // Verifica duplicidade por TÍTULO (independente da data)
            final boolean existeMesmoTitulo = Files.exists(Paths.get(pasta))
                    && Files.list(Paths.get(pasta))
                    .anyMatch(p -> p.getFileName().toString().matches(".*-" + tituloSan + "\\Q" + extensao + "\\E$"));
            if (existeMesmoTitulo) {
                throw new DuplicateException("Livro já foi salvo anteriormente em " + pasta + " (título já existente).");
            }

            final Path caminho = Paths.get(pasta + hoje() + "-" + tituloSan + extensao);
            try (BufferedWriter bw = Files.newBufferedWriter(caminho)) {
                if (txt) {
                    bw.write(livro.toString());
                } else {
                    bw.write(String.format("%d;%s;%s;%d;%s%n",
                            livro.getId(), livro.getTitulo(), livro.getAutor(),
                            livro.getAnoPublicacao(), livro.getGenero()));
                }
            }
            System.out.println("Livro salvo com sucesso em: " + caminho);
        } catch (IOException e) {
            throw new NotFoundException("Erro ao salvar livro: " + e.getMessage());
        }
    }

    // -------------------- Cadastro --------------------
    public Livro adicionarLivro(String titulo, String autor, int ano, String genero) {
        final int id = ++ultimoId;
        final Livro livro = new Livro(id, titulo, autor, ano, genero);
        if (set.contains(livro)) throw new DuplicateException("Livro duplicado (título e autor).");

        lista.add(livro);
        set.add(livro);
        map.put(id, livro);
        return livro;
    }

    // -------------------- Remoção --------------------
    public void removerPorId(int id) {
        final Livro l = map.remove(id);
        if (l == null) throw new NotFoundException("Livro não encontrado.");
        lista.remove(l);
        set.remove(l);
        // Se o livro estiver na fila, remove também
        filaEmprestimos.remove(l);
        System.out.println("Livro removido: " + l);
    }

    public void removerPorTitulo(String titulo) {
        final List<Livro> encontrados = buscarPorTitulo(titulo);
        if (encontrados.isEmpty()) throw new NotFoundException("Nenhum livro encontrado com este título.");
        if (encontrados.size() == 1) {
            removerPorId(encontrados.getFirst().getId());
            return;
        }
        // Caso mais de um, força escolha por ID
        System.out.println("Vários livros encontrados, escolha o ID para remover:");
        encontrados.forEach(System.out::println);
        final Scanner sc = new Scanner(System.in);
        System.out.print("ID: ");
        final int id = sc.nextInt();
        removerPorId(id);
    }

    public void removerPorAutor(String autor) {
        final List<Livro> encontrados = buscarPorAutor(autor);
        if (encontrados.isEmpty()) throw new NotFoundException("Nenhum livro encontrado deste autor.");
        if (encontrados.size() == 1) {
            removerPorId(encontrados.getFirst().getId());
            return;
        }
        System.out.println("Livros encontrados, escolha o ID para remover:");
        encontrados.forEach(System.out::println);
        final Scanner sc = new Scanner(System.in);
        System.out.print("ID: ");
        final int id = sc.nextInt();
        removerPorId(id);
    }

    // -------------------- Busca --------------------
    public Livro buscarPorId(int id) {
        return map.get(id);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        validarTermo(titulo);
        final String t = titulo.toLowerCase();
        return lista.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Livro> buscarPorAutor(String autor) {
        validarTermo(autor);
        final String t = autor.toLowerCase();
        return lista.stream()
                .filter(l -> l.getAutor().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    // -------------------- Listagem/Ordenação --------------------
    public void listarOrdenadoAno() {
        lista.stream()
                .sorted(Comparator.comparingInt(Livro::getAnoPublicacao))
                .forEach(System.out::println);
    }

    public void listarOrdenadoTitulo() {
        lista.stream()
                .sorted(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);
    }

    public void listarOrdenadoAutor() {
        lista.stream()
                .sorted(Comparator.comparing(Livro::getAutor, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);
    }

    public void listarTodos() { lista.forEach(System.out::println); }

    // -------------------- Empréstimos/Devoluções --------------------
    public void emprestar(int id) {
        final Livro l = map.get(id);
        if (l == null) throw new NotFoundException("Livro não encontrado.");
        filaEmprestimos.add(l);
        System.out.println("Livro emprestado: " + l);
    }

    /** Devolve o PRÓXIMO da fila (FIFO) */
    public void devolver() {
        final Livro l = filaEmprestimos.poll();
        if (l == null) {
            System.out.println("Fila de empréstimos vazia.");
        } else {
            historicoDevolucoes.push(l);
            System.out.println("Livro devolvido: " + l);
        }
    }

    /** Devolve um livro específico da fila por ID (busca dentro da fila). */
    public void devolverPorId(int id) {
        final Iterator<Livro> it = filaEmprestimos.iterator();
        while (it.hasNext()) {
            final Livro l = it.next();
            if (l.getId() == id) {
                it.remove();            // remove da fila (mesmo não sendo o primeiro)
                historicoDevolucoes.push(l);
                System.out.println("Livro devolvido: " + l);
                return;
            }
        }
        throw new NotFoundException("Livro não está na fila de empréstimos (ID: " + id + ").");
    }

    public List<Livro> buscarNaFilaPorTitulo(String titulo) {
        validarTermo(titulo);
        final String t = titulo.toLowerCase();
        return filaEmprestimos.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Livro> buscarNaFilaPorAutor(String autor) {
        validarTermo(autor);
        final String t = autor.toLowerCase();
        return filaEmprestimos.stream()
                .filter(l -> l.getAutor().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public void listarFilaEmprestimos() {
        if (filaEmprestimos.isEmpty()) {
            System.out.println("(fila vazia)");
            return;
        }
        System.out.println("Fila de empréstimos (ordem de saída):");
        filaEmprestimos.forEach(System.out::println);
    }

    public void listarHistorico() {
        if (historicoDevolucoes.isEmpty()) System.out.println("Nenhum livro devolvido ainda.");
        else historicoDevolucoes.forEach(System.out::println);
    }

    // -------------------- Persistência global (CSV) --------------------
    public void salvarCSV(String arquivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (Livro l : lista) {
                bw.write(String.format("%d;%s;%s;%d;%s%n",
                        l.getId(), l.getTitulo(), l.getAutor(), l.getAnoPublicacao(), l.getGenero()));
            }
            System.out.println("Dados salvos em " + arquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public void carregarCSV(String arquivo) {
        final File f = new File(arquivo);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                final String[] p = linha.split(";");
                final int id = Integer.parseInt(p[0]);
                final String titulo = p[1];
                final String autor = p[2];
                final int ano = Integer.parseInt(p[3]);
                final String genero = p[4];
                final Livro l = new Livro(id, titulo, autor, ano, genero);
                lista.add(l);
                set.add(l);
                map.put(id, l);
                if (id > ultimoId) ultimoId = id;
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar CSV: " + e.getMessage());
        }
    }

    // -------------------- Acesso p/ testes/saída --------------------
    public List<Livro> todos() { return List.copyOf(lista); }
}
