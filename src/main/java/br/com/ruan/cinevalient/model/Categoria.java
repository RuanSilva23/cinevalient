package br.com.ruan.cinevalient.model;

public enum Categoria {
    ACAO("Action"),
    AVENTURA("Adventure"),
    DRAMA("Drama"),
    COMEDIA("Comedy"),
    FICCAO("Sci-Fi"),
    CRIME("Crime");

    private String categoriaOMDB;

    Categoria(String categoriaOMDB) {
        this.categoriaOMDB = categoriaOMDB;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMDB.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
