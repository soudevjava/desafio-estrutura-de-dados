package presentation.console;

import application.services.BibliotecaService;
import domain.entities.Livro;
import domain.enums.Genero;
import domain.exceptions.LivroDuplicadoException;
import domain.exceptions.LivroNaoEncontradoException;
import presentation.dto.LivroDTO;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BibliotecaConsole {
    private final BibliotecaService bibliotecaService;
    private final Scanner scanner;
    private final MenuHandler menuHandler;

    public BibliotecaConsole(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
        this.scanner = new Scanner(System.in);
        this.menuHandler = new MenuHandler();
    }

    public void iniciar() {
        System.out.println("🏛️ === BEM-VINDO À BIBLIOTECA DIGITAL ===\n");

        int opcao;
        do {
            menuHandler.exibirMenuPrincipal();
            opcao = lerOpcao();
            processarOpcao(opcao);
        } while (opcao != 0);

        System.out.println("👋 Obrigado por usar a Biblioteca Digital!");
        scanner.close();
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void processarOpcao(int opcao) {
        System.out.println();

        try {
            switch (opcao) {
                case 1 -> adicionarLivro();
                case 2 -> removerLivro();
                case 3 -> buscarLivro();
                case 4 -> listarLivros();
                case 5 -> emprestarLivro();
                case 6 -> devolverLivro();
                case 7 -> exibirFilaEmprestimos();
                case 8 -> exibirHistoricoDevolvidos();
                case 9 -> exibirEstatisticas();
                case 0 -> System.out.println("🚪 Saindo do sistema...");
                default -> System.out.println("❌ Opção inválida! Tente novamente.");
            }
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }

        if (opcao != 0) {
            pausar();
        }
    }

    private void adicionarLivro() {
        System.out.println("📚 === ADICIONAR NOVO LIVRO ===");

        try {
            System.out.print("📖 Título: ");
            String titulo = scanner.nextLine().trim();

            if (!InputValidator.isValidTitulo(titulo)) {
                System.out.println("❌ Título inválido!");
                return;
            }

            System.out.print("✍️  Autor: ");
            String autor = scanner.nextLine().trim();

            if (!InputValidator.isValidAutor(autor)) {
                System.out.println("❌ Autor inválido!");
                return;
            }

            System.out.print("📅 Ano de publicação: ");
            int ano = Integer.parseInt(scanner.nextLine());

            if (!InputValidator.isValidAno(ano)) {
                System.out.println("❌ Ano inválido!");
                return;
            }

            InputValidator.exibirGenerosDisponiveis();
            System.out.print("🎭 Gênero: ");
            String generoStr = scanner.nextLine().trim();

            Genero genero = InputValidator.parseGenero(generoStr);
            if (genero == null) {
                System.out.println("❌ Gênero inválido!");
                return;
            }

            LivroDTO livroDTO = new LivroDTO(titulo, autor, ano, genero);
            bibliotecaService.adicionarLivro(livroDTO);
            System.out.println("✅ Livro adicionado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("❌ Ano deve ser um número válido!");
        } catch (LivroDuplicadoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void removerLivro() {
        System.out.println("🗑️ === REMOVER LIVRO ===");

        try {
            System.out.print("🔢 Digite o ID do livro: ");
            String idStr = scanner.nextLine();

            if (!InputValidator.isValidId(idStr)) {
                System.out.println("❌ ID inválido!");
                return;
            }

            int id = Integer.parseInt(idStr);
            bibliotecaService.removerLivro(id);
            System.out.println("✅ Livro removido com sucesso!");

        } catch (LivroNaoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void buscarLivro() {
        System.out.println("🔍 === BUSCAR LIVRO ===");
        System.out.print("📖 Digite o título (ou parte dele): ");
        String titulo = scanner.nextLine().trim();

        if (titulo.isEmpty()) {
            System.out.println("❌ Título não pode estar vazio!");
            return;
        }

        List<Livro> encontrados = bibliotecaService.buscarPorTitulo(titulo);

        if (encontrados.isEmpty()) {
            System.out.println("❌ Nenhum livro encontrado!");
        } else {
            System.out.println("📚 Livros encontrados:");
            encontrados.forEach(System.out::println);
        }
    }

    private void listarLivros() {
        List<Livro> livros = bibliotecaService.listarTodosOrdenadosPorAno();

        if (livros.isEmpty()) {
            System.out.println("📚 Biblioteca vazia!");
        } else {
            System.out.println("📚 === TODOS OS LIVROS (Ordenados por Ano) ===");
            livros.forEach(System.out::println);
        }
    }

    private void emprestarLivro() {
        System.out.println("📋 === EMPRESTAR LIVRO ===");

        try {
            System.out.print("🔢 Digite o ID do livro: ");
            String idStr = scanner.nextLine();

            if (!InputValidator.isValidId(idStr)) {
                System.out.println("❌ ID inválido!");
                return;
            }

            int id = Integer.parseInt(idStr);
            bibliotecaService.emprestarLivro(id);
            System.out.println("✅ Livro adicionado à fila de empréstimos!");

        } catch (LivroNaoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void devolverLivro() {
        if (bibliotecaService.devolverLivro()) {
            System.out.println("✅ Livro devolvido com sucesso!");
        } else {
            System.out.println("❌ Não há livros para devolver!");
        }
    }

    private void exibirFilaEmprestimos() {
        List<Livro> fila = bibliotecaService.obterFilaEmprestimos();

        if (fila.isEmpty()) {
            System.out.println("📋 Fila de empréstimos vazia!");
        } else {
            System.out.println("📋 === FILA DE EMPRÉSTIMOS ===");
            for (int i = 0; i < fila.size(); i++) {
                System.out.println((i + 1) + "º - " + fila.get(i));
            }
        }
    }

    private void exibirHistoricoDevolvidos() {
        List<Livro> historico = bibliotecaService.obterHistoricoDevolvidos();

        if (historico.isEmpty()) {
            System.out.println("📚 Histórico de devoluções vazio!");
        } else {
            System.out.println("📚 === HISTÓRICO DE DEVOLUÇÕES ===");
            historico.forEach(System.out::println);
        }
    }

    @SuppressWarnings("unchecked")
    private void exibirEstatisticas() {
        Map<String, Object> stats = bibliotecaService.obterEstatisticas();

        System.out.println("📊 === ESTATÍSTICAS DA BIBLIOTECA ===");
        System.out.println("📚 Total de livros: " + stats.get("totalLivros"));
        System.out.println("📋 Livros na fila: " + stats.get("filaEmprestimos"));
        System.out.println("🔄 Livros devolvidos: " + stats.get("historicoDevolvidos"));

        Map<String, Long> generos = (Map<String, Long>) stats.get("livrosPorGenero");
        if (!generos.isEmpty()) {
            System.out.println("\n📊 Livros por gênero:");
            generos.forEach((genero, quantidade) ->
                    System.out.println("  " + genero + ": " + quantidade));
        }
    }

    private void pausar() {
        System.out.println("\n⏸️  Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}