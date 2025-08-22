import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Biblioteca {

    private static final List<Livro> listaDeLivros = new ArrayList<>();
    private static final Map<Integer, Livro> livroPorId = new HashMap<>();
    private static final Queue<Livro> filaDeEmprestimos = new LinkedList<>();
    private static final Stack<Livro> historicoDeEmprestimos = new Stack<>();

    public void adicionarLivro(Livro livro){

        Set<Livro> livroSet = new HashSet<>(listaDeLivros);

        if (livroPorId.containsKey(livro.getId())){
            System.out.println("Livro já cadastrado na biblioteca.");
        }else {
            listaDeLivros.add(livro);
            livroSet.add(livro);
            livroPorId.put(livro.getId(), livro);

            System.out.println("Livro " + livro.getTitulo() + " adicionado à biblioteca: ");

        }


    }
     public void listarLivros(){
        if (Biblioteca.listaDeLivros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado na biblioteca.");
        } else {
            System.out.println("Lista de Livros na Biblioteca:");
            System.out.println(listaDeLivros);

        }
    }
     public void buscarLivroPorTitulo(String titulo){
        for (Livro livro : Biblioteca.listaDeLivros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                System.out.println("Livro encontrado: ID: " + livro.getId() +
                        ", Título: " + livro.getTitulo() +
                        ", Autor: " + livro.getAutor() +
                        ", Ano de Publicação: " + livro.getAnoPublicacao() +
                        ", Gênero: " + livro.getGenero());
                return;
            }
        }
        System.out.println("Livro não encontrado.");
    }

     public void listarLivrosOrdenadosPorAno(){
        List<Livro> livrosOrdenados = new ArrayList<>(listaDeLivros);
        livrosOrdenados.sort(Comparator.comparingInt(Livro::getAnoPublicacao));
        for (Livro livro : livrosOrdenados) {
            System.out.println("ID: " + livro.getId() +
                    ", Título: " + livro.getTitulo() +
                    ", Autor: " + livro.getAutor() +
                    ", Ano de Publicação: " + livro.getAnoPublicacao() +
                    ", Gênero: " + livro.getGenero());
        }
    }


    public void removerLivroPorId(int id){

        if (!livroPorId.containsKey(id)){
            System.out.println("Livro não encontrado.");
            return;
        }
        listaDeLivros.remove(livroPorId.get(id));
        System.out.println("Livro com ID " + id + " removido com sucesso.");

    }

    public void emprestarLivro(int id){

        System.out.println("Digitou o ID do livro a ser emprestado: " + id);

        listaDeLivros.stream().filter(l -> l.getId() == id ).findFirst().map(filaDeEmprestimos::add).orElseGet(() ->{
            System.out.println("Livro não encontrado para empréstimo.");
            return null;
        });

        for(Livro livro : listaDeLivros){
            if (livro.getId() == id) {
                filaDeEmprestimos.add(livro);
                System.out.println("Livro adicionado à fila de empréstimos: " + livro.getTitulo());
               // return;
            }else {
                System.out.println("Livro não encontrado para empréstimo.");
            }
        }


    }
    public void devolverLivro(int id){

        System.out.println("Digito ai ID do livro a ser devolvido: " + id);

        Livro livroPraDevolver = null;
        boolean livroEncontrado = false;

        for (Livro livro : listaDeLivros) {
            if (livro.getId() == id) {
                livroPraDevolver = livro;
                livroEncontrado = true;
                break;
            }
        }
        if (livroEncontrado){
            boolean foiRemovido = filaDeEmprestimos.removeIf(l-> l.getId() == id);
            if (foiRemovido){
                historicoDeEmprestimos.push(livroPraDevolver);
                System.out.println("Livro devolvido com sucesso: " + livroPraDevolver.getTitulo());
            }else {
                System.out.println("Livro não está emprestado");
            }
        }else {
            System.out.println("Livro com o ID: " + " não encontrado para remoção.");
        }
    }



}
