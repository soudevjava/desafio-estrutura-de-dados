package utils;

import model.Livro;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivo {
    private static final String DELIMITADOR = ",";

    public static void salvarLivros(List<Livro> livros, String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, StandardCharsets.UTF_8))) {
            for (Livro livro : livros) {
                String linha = String.join(DELIMITADOR,
                        String.valueOf(livro.getId()),
                        livro.getTitulo(),
                        livro.getAutor(),
                        String.valueOf(livro.getAnoPublicacao()),
                        livro.getGenero());
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar os livros no arquivo: " + e.getMessage());
        }
    }

    public static List<Livro> carregarLivros(String nomeArquivo) {
        List<Livro> livros = new ArrayList<>();

        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) {
            return livros;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(DELIMITADOR);
                if (dados.length == 5) {
                    try {
                        int id = Integer.parseInt(dados[0]);
                        String titulo = dados[1];
                        String autor = dados[2];
                        int ano = Integer.parseInt(dados[3]);
                        String genero = dados[4];
                        livros.add(new Livro(id, titulo, autor, ano, genero));

                    } catch (NumberFormatException e) {
                        System.err.println("Aviso: Linha com formato numérico inválido ignorada: " + linha);
                    }
                } else {
                    System.err.println("Aviso: Linha malformada ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar os livros do arquivo: " + e.getMessage());
        }
        return livros;
    }
}
