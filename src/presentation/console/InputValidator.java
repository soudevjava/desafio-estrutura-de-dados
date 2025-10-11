package presentation.console;

import domain.enums.Genero;

public class InputValidator {

    public static boolean isValidTitulo(String titulo) {
        return titulo != null && !titulo.trim().isEmpty() && titulo.length() <= 200;
    }

    public static boolean isValidAutor(String autor) {
        return autor != null && !autor.trim().isEmpty() && autor.length() <= 100;
    }

    public static boolean isValidAno(int ano) {
        return ano >= 1000 && ano <= 2024;
    }

    public static boolean isValidId(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Genero parseGenero(String generoStr) {
        try {
            return Genero.fromDescricao(generoStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void exibirGenerosDisponiveis() {
        System.out.println("\n📚 Gêneros disponíveis:");
        for (Genero genero : Genero.values()) {
            System.out.println("  • " + genero.getDescricao());
        }
    }
}