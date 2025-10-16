package br.com.ruan.cinevalient.service;

public enum ConverteOpcoes {
    BUSCA_SERIE,
    BUSCA_EPISODIO,
    LISTAR_SERIES_BUSCADAS,
    SAIR;

    public static ConverteOpcoes fromCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> BUSCA_SERIE;
            case 2 -> BUSCA_EPISODIO;
            case 3 -> LISTAR_SERIES_BUSCADAS;
            case 0 -> SAIR;
            default -> throw new IllegalArgumentException("Opção inválida: " + codigo);
        };
    }
}
