package model;

import java.util.*;
import java.util.stream.Collectors;

public class Biblioteca {

    private final List<Livro> listaLivros = new ArrayList<>();

    private final Set<Livro> setLivros = new HashSet<>();

    private final Map<Integer, Livro> mapLivros = new HashMap<>();

    private final Queue<Livro> queueLivros = new LinkedList<>();

    private final Stack<Livro> stackLivros = new Stack<>();

    public Biblioteca(List<Livro> livrosIniciais) {
        livrosIniciais.forEach(this::adicionarLivro);

        int maiorId = livrosIniciais.stream()
                .mapToInt(Livro::getId)
                .max()
                .orElse(0);

        Livro.atualizarProximoId(maiorId);
    }


    public void adicionarLivro(Livro livro) {
        if (this.setLivros.contains(livro)) throw new RuntimeException("Livro duplicado " + livro.toString());
        this.listaLivros.add(livro);
        this.setLivros.add(livro);
        this.mapLivros.put(livro.getId(), livro);
    }

    public void removerLivroPorId(int id){
        Livro livro = this.buscarLivroPorId(id);
        if (livro == null) throw new RuntimeException("Não existe livro com cadastrado com id: " + id);
        this.listaLivros.remove(livro);
        this.setLivros.remove(livro);
        this.mapLivros.remove(id);
    }

    public Livro buscarLivroPorTitulo(String titulo){
        for(Livro livro : this.listaLivros){
            if(livro.getTitulo().equals(titulo)){
                return livro;
            }
        }
        return null;
    }

    public Livro buscarLivroPorId(int id){
       Livro livro = this.mapLivros.get(id);
       if(livro == null) throw new RuntimeException("Livro não encontrado!");
       return livro;
    }

    public void listarLivrosOrdenadosPorAno(){
         this.listaLivros
             .stream()
             .sorted(Comparator.comparing(Livro::getAnoPublicacao))
             .forEach(System.out::println);
    }

    public void listarLivrosOrdenadosPorTitulo(){
        this.listaLivros
                .stream()
                .sorted(Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);
    }

    public void listarLivrosOrdenadosPorGenero(){
        this.listaLivros
                .stream()
                .sorted(Comparator.comparing(Livro::getGenero, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);
    }

    public void listarLivrosOrdenadosPorId(){
        this.listaLivros
                .stream()
                .sorted(Comparator.comparing(Livro::getId))
                .forEach(System.out::println);
    }

    public void emprestarLivro(int id){
        Livro livro = this.buscarLivroPorId(id);
        this.queueLivros.add(livro);
    }

    public List<Livro> getLivrosOrdenados(Comparator<Livro> comparador) {
        return listaLivros.stream()
                .sorted(comparador)
                .collect(Collectors.toList());
    }

    public Livro devolverLivro(){
        Livro livro = this.queueLivros.poll();
        if(livro == null) throw new RuntimeException("Não há livros na fila de emprestimo!");
        this.stackLivros.push(livro);
        return livro;
    }

    public List<Livro> getTodosOsLivros() {
        return this.listaLivros;
    }

}
