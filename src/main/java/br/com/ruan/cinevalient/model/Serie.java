package br.com.ruan.cinevalient.model;

import br.com.ruan.cinevalient.service.traducao.ConsultaMyMemory;

import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private String ano;
    private Categoria genero;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String sinopse;
    private String atores;

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.ano = dadosSerie.ano();
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
        this.atores = dadosSerie.atores();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    @Override
    public String toString() {
        return  "gênero: " + genero +
                "\ntitulo: " + titulo +
                "\nano: " + ano  +
                "\navaliação: " + avaliacao +
                "\ntemporadas: " + totalTemporadas +
                "\natores: " + atores +
                "\nsinopse: " + sinopse +
                "\n"
                ;
    }
}
