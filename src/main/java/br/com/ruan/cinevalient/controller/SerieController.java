package br.com.ruan.cinevalient.controller;

import br.com.ruan.cinevalient.dto.SerieDTO;
import br.com.ruan.cinevalient.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {
    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("/series")
    public List<SerieDTO> obterSeries() {
        return serieRepository.findAll().stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getAno(), s.getGenero(), s.getTotalTemporadas(), s.getAvaliacao(), s.getSinopse(), s.getAtores(), s.getPoster()))
                .collect(Collectors.toList());
    }

}
