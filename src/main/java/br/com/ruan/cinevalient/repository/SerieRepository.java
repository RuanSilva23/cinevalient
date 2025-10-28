package br.com.ruan.cinevalient.repository;

import br.com.ruan.cinevalient.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
