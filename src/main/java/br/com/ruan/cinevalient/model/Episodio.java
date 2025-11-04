package br.com.ruan.cinevalient.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodio")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temporada;

    private String titulo;

    private Integer numero;

    private Double avaliacao;

    private LocalDate dataLancamento;

    private String resumo;

    @ManyToOne
    private Serie serie;

    public Episodio(){}

    public Episodio(Integer numeroTemporada, DadosEpisodios dadosEpisodios){
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodios.titulo();
        this.numero = dadosEpisodios.numero();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodios.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodios.dataLancamento());

        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada + ", titulo=" + titulo + ", epísodio=" + numero + ", avaliacao=" + avaliacao + ", dataLancamento=" + dataLancamento  + "\n";
    }
}
