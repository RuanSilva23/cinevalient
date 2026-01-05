package br.com.ruan.cinevalient.service;

public enum ConverteOpcoes {
    BUSCA_SERIE,
    BUSCA_EPISODIO,
    LISTAR_SERIES_BUSCADAS,
    BUSCAR_SERIE_POR_TITULO,
    BUSCAR_SERIE_POR_ATOR,
    TOP_5_SERIES,
    BUSCAR_SERIE_CATEGORIA,
    BUSCAR_SERIE_POR_TEMPORADA_E_AVALIACAO,
    BUSCAR_SERIE_TRECHO,
    TOP_EPISODIO_SERIE,
    BUSCA_EPISODIO_DATA_LANCAMENTO,
    SAIR;

    public static ConverteOpcoes fromCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> BUSCA_SERIE;
            case 2 -> BUSCA_EPISODIO;
            case 3 -> LISTAR_SERIES_BUSCADAS;
            case 4 -> BUSCAR_SERIE_POR_TITULO;
            case 5 -> BUSCAR_SERIE_POR_ATOR;
            case 6 -> TOP_5_SERIES;
            case 7 -> BUSCAR_SERIE_CATEGORIA;
            case 8 -> BUSCAR_SERIE_POR_TEMPORADA_E_AVALIACAO;
            case 9 -> BUSCAR_SERIE_TRECHO;
            case 10 -> TOP_EPISODIO_SERIE;
            case 11 -> BUSCA_EPISODIO_DATA_LANCAMENTO;
            case 0 -> SAIR;
            default -> throw new IllegalArgumentException("Opção inválida: " + codigo);
        };
    }
}
