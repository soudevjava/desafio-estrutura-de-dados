package run;


import exceptions.NotFoundException;
import model.Livro;
import service.Biblioteca;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarCSV("biblioteca.csv");

        // Adiciona 10 best-sellers iniciais se a biblioteca estiver vazia
        if (biblioteca.todos().isEmpty()) {
            biblioteca.adicionarLivro("O Código Da Vinci", "Dan Brown", 2003, "Suspense");
            biblioteca.adicionarLivro("Anjos e Demônios", "Dan Brown", 2000, "Suspense");
            biblioteca.adicionarLivro("Harry Potter e a Pedra Filosofal", "J.K. Rowling", 1997, "Fantasia");
            biblioteca.adicionarLivro("Harry Potter e as Relíquias da Morte", "J.K. Rowling", 2007, "Fantasia");
            biblioteca.adicionarLivro("O Senhor dos Anéis: A Sociedade do Anel", "J.R.R. Tolkien", 1954, "Fantasia");
            biblioteca.adicionarLivro("O Hobbit", "J.R.R. Tolkien", 1937, "Fantasia");
            biblioteca.adicionarLivro("A Menina que Roubava Livros", "Markus Zusak", 2005, "Drama");
            biblioteca.adicionarLivro("O Alquimista", "Paulo Coelho", 1988, "Ficção");
            biblioteca.adicionarLivro("A Culpa é das Estrelas", "John Green", 2012, "Romance");
            biblioteca.adicionarLivro("O Pequeno Príncipe", "Antoine de Saint-Exupéry", 1943, "Infantil");
        }

        final Scanner sc = new Scanner(System.in);
        int opcao = -1;

        do {
            System.out.println("""
                ==== Biblioteca Digital ====
                1 - Adicionar livro (ID automático)
                2 - Remover livro (por id/título/autor)
                3 - Buscar livro (por id/título/autor)
                4 - Listar livros por ano
                5 - Emprestar livro (por id/título/autor)
                6 - Devolver livro (por id/título/autor) ou devolver próximo
                7 - Listar por título
                8 - Listar por autor
                9 - Fila de empréstimos
                10 - Histórico de devoluções
                0 - Sair
                """);
            System.out.print("Escolha: ");
            final String entrada = sc.nextLine().trim();

            try {
                // Valida se é número
                opcao = Integer.parseInt(entrada);

                // Fora do intervalo válido
                if (opcao < 0 || opcao > 10) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                switch (opcao) {
                    case 1 -> { // Adicionar + persistência opcional por livro
                        System.out.print("Título: "); final String t = sc.nextLine();
                        System.out.print("Autor: "); final String a = sc.nextLine();
                        System.out.print("Ano: "); final int ano = sc.nextInt(); sc.nextLine();
                        System.out.print("Gênero: "); final String g = sc.nextLine();

                        final Livro livro = biblioteca.adicionarLivro(t, a, ano, g);
                        System.out.println("Adicionado: " + livro);

                        System.out.print("Deseja salvar este livro em arquivo? (s/n): ");
                        final String salvar = sc.nextLine();
                        if (salvar.equalsIgnoreCase("s")) {
                            System.out.print("Escolha o tipo (txt/csv): ");
                            final String tipo = sc.nextLine();
                            biblioteca.salvarLivroEmArquivo(livro, tipo);
                        }
                    }

                    case 2 -> { // Remover
                        System.out.print("Remover por (id/titulo/autor): ");
                        final String modo = sc.nextLine().toLowerCase();
                        switch (modo) {
                            case "id" -> {
                                System.out.print("ID: "); final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.removerPorId(id);
                            }
                            case "titulo" -> {
                                System.out.print("Título: "); final String titulo = sc.nextLine();
                                biblioteca.removerPorTitulo(titulo);
                            }
                            case "autor" -> {
                                System.out.print("Autor: "); final String autor = sc.nextLine();
                                biblioteca.removerPorAutor(autor);
                            }
                            default -> System.out.println("Opção inválida.");
                        }
                    }

                    case 3 -> { // Buscar
                        System.out.print("Buscar por (id/titulo/autor): ");
                        final String modo = sc.nextLine().toLowerCase();
                        switch (modo) {
                            case "id" -> {
                                System.out.print("ID: "); final int id = sc.nextInt(); sc.nextLine();
                                final Livro l = biblioteca.buscarPorId(id);
                                if (l == null) System.out.println("Não encontrado.");
                                else System.out.println(l);
                            }
                            case "titulo" -> {
                                System.out.print("Título: "); final String titulo = sc.nextLine();
                                final List<Livro> l = biblioteca.buscarPorTitulo(titulo);
                                if (l.isEmpty()) System.out.println("Nenhum livro encontrado.");
                                else l.forEach(System.out::println);
                            }
                            case "autor" -> {
                                System.out.print("Autor: "); final String autor = sc.nextLine();
                                final List<Livro> l = biblioteca.buscarPorAutor(autor);
                                if (l.isEmpty()) System.out.println("Nenhum livro encontrado.");
                                else l.forEach(System.out::println);
                            }
                            default -> System.out.println("Opção inválida.");
                        }
                    }

                    case 4 -> biblioteca.listarOrdenadoAno();

                    case 5 -> { // Emprestar por id/título/autor
                        System.out.print("Emprestar por (id/titulo/autor): ");
                        final String modo = sc.nextLine().toLowerCase();
                        switch (modo) {
                            case "id" -> {
                                System.out.print("ID: "); final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.emprestar(id);
                            }
                            case "titulo" -> {
                                System.out.print("Título: "); final String titulo = sc.nextLine();
                                final List<Livro> candidatos = biblioteca.buscarPorTitulo(titulo);
                                if (candidatos.isEmpty()) throw new NotFoundException("Nenhum livro com este título.");
                                candidatos.forEach(System.out::println);
                                System.out.print("Informe o ID para emprestar: ");
                                final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.emprestar(id);
                            }
                            case "autor" -> {
                                System.out.print("Autor: "); final String autor = sc.nextLine();
                                final List<Livro> candidatos = biblioteca.buscarPorAutor(autor);
                                if (candidatos.isEmpty()) throw new NotFoundException("Nenhum livro deste autor.");
                                candidatos.forEach(System.out::println);
                                System.out.print("Informe o ID para emprestar: ");
                                final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.emprestar(id);
                            }
                            default -> System.out.println("Opção inválida.");
                        }
                    }

                    case 6 -> { // Devolver por id/título/autor OU próximo
                        System.out.print("Devolver por (id/titulo/autor) ou 'proximo' para o primeiro da fila: ");
                        final String modo = sc.nextLine().toLowerCase();
                        switch (modo) {
                            case "proximo" -> biblioteca.devolver();
                            case "id" -> {
                                biblioteca.listarFilaEmprestimos();
                                System.out.print("ID: "); final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.devolverPorId(id);
                            }
                            case "titulo" -> {
                                System.out.print("Título: "); final String titulo = sc.nextLine();
                                final List<Livro> naFila = biblioteca.buscarNaFilaPorTitulo(titulo);
                                if (naFila.isEmpty()) throw new NotFoundException("Nenhum livro com este título na fila.");
                                naFila.forEach(System.out::println);
                                System.out.print("Informe o ID para devolver: ");
                                final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.devolverPorId(id);
                            }
                            case "autor" -> {
                                System.out.print("Autor: "); final String autor = sc.nextLine();
                                final List<Livro> naFila = biblioteca.buscarNaFilaPorAutor(autor);
                                if (naFila.isEmpty()) throw new NotFoundException("Nenhum livro deste autor na fila.");
                                naFila.forEach(System.out::println);
                                System.out.print("Informe o ID para devolver: ");
                                final int id = sc.nextInt(); sc.nextLine();
                                biblioteca.devolverPorId(id);
                            }
                            default -> System.out.println("Opção inválida.");
                        }
                    }

                    case 7 -> biblioteca.listarOrdenadoTitulo();
                    case 8 -> biblioteca.listarOrdenadoAutor();
                    case 9 -> biblioteca.listarFilaEmprestimos();
                    case 10 -> biblioteca.listarHistorico();

                    case 0 -> {
                        biblioteca.salvarCSV("biblioteca.csv");
                        System.out.println("Saindo e salvando biblioteca...");
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
            } catch (Exception e) {
                System.out.println("[Erro] " + e.getMessage());
            }
        } while (opcao != 0);

        sc.close();
    }
}
