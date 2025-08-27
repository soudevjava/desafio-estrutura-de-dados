import model.Biblioteca;
import model.Livro;
import utils.GerenciadorDeArquivo;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String NOME_ARQUIVO = "biblioteca.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static Biblioteca biblioteca;

    public static void main(String[] args) {

        System.out.println("Hello and welcome!");



//        Livro[] livro = {new Livro("Dom Quixote", "Miguel de Cervantes", 1605, "Romance"),
//                new Livro("1984", "George Orwell", 1949, "Distopia"),
//                new Livro("O Pequeno Príncipe", "Antoine de Saint-Exupéry", 1943, "Infantil"),
//                new Livro("Harry Potter e a Pedra Filosofal", "J.K. Rowling", 1997, "Fantasia"),
//                new Livro("O Senhor dos Anéis", "J.R.R. Tolkien", 1954, "Fantasia")
//        };
//
       // biblioteca = new Biblioteca(List.of(livro));

        List<Livro> livrosSalvos = GerenciadorDeArquivo.carregarLivros(NOME_ARQUIVO);

        biblioteca = new Biblioteca(livrosSalvos);
        System.out.println(livrosSalvos.size() + " livros carregados do arquivo.");

        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Erro: Por favor, digite um número válido.");
                opcao = -1;
            } finally {
                scanner.nextLine();
            }

            selecionarOpcao(opcao);
        }

        GerenciadorDeArquivo.salvarLivros(biblioteca.getTodosOsLivros(), NOME_ARQUIVO);
        System.out.println("Biblioteca salva. Até logo!");
        scanner.close();
    }


    private static void exibirMenu() {
        System.out.println("\n--- Menu da Biblioteca ---");
        System.out.println("1. Adicionar livro");
        System.out.println("2. Remover livro por ID");
        System.out.println("3. Buscar livro por título");
        System.out.println("4. Listar livros (várias ordenações)");
        System.out.println("5. Emprestar livro por ID");
        System.out.println("6. Devolver próximo livro da fila");
        System.out.println("0. Salvar e Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void selecionarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> adicionarLivro();
            case 2 -> removerLivro();
            case 3 -> buscarLivro();
            case 4 -> menuListarLivros();
            case 5 -> emprestarLivro();
            case 6 -> devolverLivro();
            case 0 -> {}
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void adicionarLivro() {
        try {
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Ano de Publicação: ");
            int ano = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Gênero: ");
            String genero = scanner.nextLine();

            biblioteca.adicionarLivro(new Livro(titulo, autor, ano, genero));
            System.out.println("Livro adicionado com sucesso!");
        } catch (InputMismatchException e) {
            System.err.println("Erro: O ano deve ser um número.");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Erro ao adicionar: " + e.getMessage());
        }
    }

    private static void removerLivro() {
        try {
            System.out.print("Digite o ID do livro para remover: ");
            int id = scanner.nextInt();
            biblioteca.removerLivroPorId(id);
            System.out.println("Livro removido com sucesso!");
        } catch (InputMismatchException e) {
            System.err.println("Erro: O ID deve ser um número.");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Erro ao remover: " + e.getMessage());
        }
    }

    private static void buscarLivro() {
        System.out.print("Digite o título para buscar: ");
        String titulo = scanner.nextLine();
        Livro livro = biblioteca.buscarLivroPorTitulo(titulo);
        if (livro != null) {
            System.out.println("Livro encontrado:\n" + livro);
        } else {
            System.out.println("Nenhum livro encontrado com esse título.");
        }
    }

    private static void menuListarLivros() {
        System.out.println("\n--- Opções de Ordenação ---");
        System.out.println("1. Por Ano de Publicação (crescente)");
        System.out.println("2. Por Título (A-Z)");
        System.out.println("3. Por Autor (A-Z)");
        System.out.print("Escolha como deseja ordenar: ");

        try {
            int subOpcao = scanner.nextInt();
            scanner.nextLine();
            Comparator<Livro> comparador;
            switch (subOpcao) {
                case 1 -> comparador = Comparator.comparingInt(Livro::getAnoPublicacao);
                case 2 -> comparador = Comparator.comparing(Livro::getTitulo, String.CASE_INSENSITIVE_ORDER);
                case 3 -> comparador = Comparator.comparing(Livro::getAutor, String.CASE_INSENSITIVE_ORDER);
                default -> {
                    System.out.println("Opção de ordenação inválida.");
                    return;
                }
            }
            List<Livro> livrosOrdenados = biblioteca.getLivrosOrdenados(comparador);
            System.out.println("\n--- Lista de Livros ---");
            livrosOrdenados.forEach(System.out::println);
            System.out.println("-------------------------");

        } catch (InputMismatchException e) {
            System.err.println("Erro: Opção inválida.");
            scanner.nextLine();
        }
    }

    private static void emprestarLivro() {
        try {
            System.out.print("Digite o ID do livro para emprestar: ");
            int id = scanner.nextInt();
            biblioteca.emprestarLivro(id);
            System.out.println("Livro adicionado à fila de empréstimos.");
        } catch (InputMismatchException e) {
            System.err.println("Erro: O ID deve ser um número.");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Erro ao emprestar: " + e.getMessage());
        }
    }

    private static void devolverLivro() {
        try {
            Livro livroDevolvido = biblioteca.devolverLivro();
            System.out.println("Livro devolvido com sucesso: " + livroDevolvido.getTitulo());
        } catch (Exception e) {
            System.err.println("Erro ao devolver: " + e.getMessage());
        }
    }
}