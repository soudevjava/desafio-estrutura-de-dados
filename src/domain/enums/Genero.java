package domain.enums;

public enum Genero {
    FICCAO_CIENTIFICA("Ficção Científica"),
    ROMANCE("Romance"),
    FANTASIA("Fantasia"),
    MISTERIO("Mistério"),
    BIOGRAFIA("Biografia"),
    HISTORIA("História"),
    TECNOLOGIA("Tecnologia"),
    INFANTIL("Infantil"),
    DRAMA("Drama"),
    AVENTURA("Aventura"),
    FILOSOFIA("Filosofia"),
    CIENCIAS("Ciências");

    private final String descricao;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Genero fromDescricao(String descricao) {
        for (Genero genero : values()) {
            if (genero.getDescricao().equalsIgnoreCase(descricao)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Gênero não encontrado: " + descricao);
    }
}
