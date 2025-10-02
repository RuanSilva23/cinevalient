package br.com.ruan.cinevalient.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
