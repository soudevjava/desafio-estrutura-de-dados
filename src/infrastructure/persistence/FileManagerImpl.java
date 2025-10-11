package infrastructure.persistence;

import domain.entities.Livro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManagerImpl implements FileManager {
    private static final String ARQUIVO_LIVROS = "biblioteca.txt";
    private static final String CHARSET = "UTF-8";

    @Override
    public void salvarLivros(List<Livro> livros) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_LIVROS), CHARSET))) {

            for (Livro livro : livros) {
                writer.write(livro.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    @Override
    public List<Livro> carregarLivros() {
        List<Livro> livros = new ArrayList<>();
        File arquivo = new File(ARQUIVO_LIVROS);

        if (!arquivo.exists()) {
            return livros;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), CHARSET))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    try {
                        livros.add(Livro.fromFileFormat(linha));
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha: " + linha);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }

        return livros;
    }
}