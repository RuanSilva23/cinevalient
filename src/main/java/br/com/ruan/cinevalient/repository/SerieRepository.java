package br.com.ruan.cinevalient.repository;

import br.com.ruan.cinevalient.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacaoMinima);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();
}
