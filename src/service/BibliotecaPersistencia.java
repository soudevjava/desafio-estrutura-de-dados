package service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaPersistencia {
    private static final String ARQUIVO = "biblioteca.csv";

    private BibliotecaPersistencia() {}

    public static void salvar(List<model.Livro> livros) {
        try (var writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(ARQUIVO), StandardCharsets.UTF_8))) {
            for (final var livro : livros) {
                writer.write(livro.getId() + ";" + escape(livro.getTitulo()) + ";" +
                        escape(livro.getAutor()) + ";" + livro.getAnoPublicacao() + ";" +
                        escape(livro.getGenero()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar livros: " + e.getMessage());
        }
    }

    private static String escape(String s) { return s.replace(";", ","); }

    public static List<model.Livro> carregar() {
        final List<model.Livro> livros = new ArrayList<>();
        final var file = new File(ARQUIVO);
        if (!file.exists()) return livros;

        try (var reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                final var partes = linha.split(";");
                if (partes.length == 5) {
                    final var id = Integer.parseInt(partes[0]);
                    final var titulo = partes[1];
                    final var autor = partes[2];
                    final var ano = Integer.parseInt(partes[3]);
                    final var genero = partes[4];
                    livros.add(new model.Livro(id, titulo, autor, ano, genero));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar livros: " + e.getMessage());
        }
        return livros;
    }
}

