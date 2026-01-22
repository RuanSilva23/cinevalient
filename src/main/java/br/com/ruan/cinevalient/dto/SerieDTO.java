package br.com.ruan.cinevalient.dto;

import br.com.ruan.cinevalient.model.Categoria;

public record SerieDTO( Long id,
                        String titulo,
                        String ano,
                        Categoria genero,
                        Integer totalTemporadas,
                        Double avaliacao,
                        String sinopse,
                        String atores,
                        String poster) {
}
