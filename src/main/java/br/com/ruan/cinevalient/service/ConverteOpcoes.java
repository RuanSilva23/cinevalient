package br.com.ruan.cinevalient.service;

public enum ConverteOpcoes {
    BUSCA_SERIE,
    BUSCA_EPISODIO,
    LISTAR_SERIES_BUSCADAS,
    BUSCAR_SERIE_POR_TITULO,
    BUSCAR_SERIE_POR_ATOR,
    TOP_5_SERIES,
    SAIR;

    public static ConverteOpcoes fromCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> BUSCA_SERIE;
            case 2 -> BUSCA_EPISODIO;
            case 3 -> LISTAR_SERIES_BUSCADAS;
            case 4 -> BUSCAR_SERIE_POR_TITULO;
            case 5 -> BUSCAR_SERIE_POR_ATOR;
            case 6 -> TOP_5_SERIES;
            case 0 -> SAIR;
            default -> throw new IllegalArgumentException("Opção inválida: " + codigo);
        };
    }
}
