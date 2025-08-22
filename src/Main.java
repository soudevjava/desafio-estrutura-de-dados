import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Scanner scn = new Scanner(System.in);
    public static void main(String[] args) {

        Biblioteca biblioteca = new Biblioteca();


        System.out.println("================= Bem-vindo à Biblioteca! ==================");
        int operador = 0;
        while (operador != 7) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Remover Livro: ");
            System.out.println("3. Buscar Livro por Titulo: ");
            System.out.println("4. Listar todos os livros (ordenados por ano): ");
            System.out.println("5. Emprestar Livro");
            System.out.println("6. Devolver Livro");
            System.out.println("7. Sair");
            System.out.println("================= ###### ==================");

            operador = scn.nextInt();
            scn.nextLine();

            switch (operador) {
                case 1:
                    System.out.println("Digite o ID do livro:  ");
                    int idLivro =scn.nextInt();
                    scn.nextLine();
                    System.out.println("Digite o título do livro: ");
                    String tituloLivro = scn.nextLine();
                    System.out.println("Digite o autor do livro: ");
                    String autorLivro = scn.nextLine();
                    System.out.println("Digite o ano de publicação do livro: ");
                    int anoPublicacaoLivro = scn.nextInt();
                    scn.nextLine();
                    System.out.println("Digite o gênero do livro: ");
                    String generoLivro = scn.nextLine();

                    Livro livro = new Livro(idLivro, tituloLivro, autorLivro, anoPublicacaoLivro, generoLivro);

                    biblioteca.adicionarLivro(livro);
                    break;
                case 2:
                    System.out.println("Digite o ID do livro a ser removido: ");
                    int idLivroRemocao = scn.nextInt();
                    scn.nextLine();
                    biblioteca.removerLivroPorId(idLivroRemocao);
                    break;
                case 3:
                    System.out.println("Digite o titulo do livro a ser buscado: ");
                    String tituloBusca = scn.nextLine();
                    biblioteca.buscarLivroPorTitulo(tituloBusca);
                    break;
                case 4:
                    System.out.println("Listando todos os livros (ordenados por ano):");
                    biblioteca.listarLivrosOrdenadosPorAno();
                    break;
                case 5:
                    System.out.println("Digite o ID do livro a ser emprestado: ");
                    int idEmprestimo = scn.nextInt();
                    scn.nextLine();
                    biblioteca.emprestarLivro(idEmprestimo);
                    break;
                case 6:
                    System.out.println("Digite o ID do livro a ser removido: ");
                    int idDevolucao = scn.nextInt();
                    scn.nextLine();
                    biblioteca.devolverLivro(idDevolucao);
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }








    }


}