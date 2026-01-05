package br.com.ruan.cinevalient.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    AVENTURA("Adventure", "Aventura"),
    DRAMA("Drama", "Drama"),
    COMEDIA("Comedy", "Comedia"),
    FICCAO("Sci-Fi", "Ficção"),
    CRIME("Crime", "Crime"),
    ROMANCE("Romance", "Romance");

    private String categoriaOMDB;
    private String categoriaEmPortugues;

    Categoria(String categoriaOMDB, String categoriaEmPortugues) {
        this.categoriaOMDB = categoriaOMDB;
        this.categoriaEmPortugues = categoriaEmPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEmPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
