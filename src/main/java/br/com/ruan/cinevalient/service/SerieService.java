package br.com.ruan.cinevalient.service;

import br.com.ruan.cinevalient.dto.EpisodioDTO;
import br.com.ruan.cinevalient.dto.SerieDTO;
import br.com.ruan.cinevalient.model.Categoria;
import br.com.ruan.cinevalient.model.Serie;
import br.com.ruan.cinevalient.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> obterSerie() {
        return converteDados(serieRepository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(serieRepository.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getAno(), s.getGenero(), s.getTotalTemporadas(), s.getAvaliacao(), s.getSinopse(), s.getAtores(), s.getPoster()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamento() {
        return converteDados(serieRepository.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getAno(), s.getGenero(), s.getTotalTemporadas(), s.getAvaliacao(), s.getSinopse(), s.getAtores(), s.getPoster());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDTO(e.getTemporada(), e.getNumero(), e.getTitulo())).collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return serieRepository.episodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumero(), e.getTitulo())).collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriePorCategoria(String categoria) {
        Categoria categoriaSerie = Categoria.fromPortugues(categoria);
        return converteDados(serieRepository.findByGenero(categoriaSerie));
    }
}
