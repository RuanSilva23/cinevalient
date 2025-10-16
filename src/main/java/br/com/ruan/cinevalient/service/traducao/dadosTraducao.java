package br.com.ruan.cinevalient.service.traducao;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record dadosTraducao(@JsonAlias(value = "responseData") DadosResposta dadosResposta ) {
}
