package br.com.ruan.cinevalient.principal;

import br.com.ruan.cinevalient.model.DadosSerie;
import br.com.ruan.cinevalient.model.DadosTemporada;
import br.com.ruan.cinevalient.model.Serie;
import br.com.ruan.cinevalient.service.ConsumoAPI;
import br.com.ruan.cinevalient.service.ConverteDados;
import br.com.ruan.cinevalient.service.ConverteOpcoes;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=7a9c54d5";

    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversoDados = new ConverteDados();

    private Scanner scanner = new Scanner(System.in);

    private List<DadosSerie> dadosSerieList = new ArrayList<>();

    public void exibeMenu() {
        int opcao;
        var menu = """
                Opcões:
                 1 - Buscar por séries
                 2 - Buscar por episódios
                 3 - Listar séries buscadas
                 0 - Sair
                """;
        do {
                System.out.println(menu);
                System.out.print("Digite a opção desejada: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                ConverteOpcoes converteOpcoes = ConverteOpcoes.fromCodigo(opcao);

                switch (converteOpcoes) {
                    case BUSCA_SERIE:
                        buscarSerieWeb();
                        break;

                    case BUSCA_EPISODIO:
                        buscarEpisodioPorSerie();
                        break;


                    case LISTAR_SERIES_BUSCADAS:
                        listarSeriesBuscadas();
                        break;

                    case SAIR:
                        System.out.println("Saindo...");
                        break;

                }
            }while (opcao != 0);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        dadosSerieList.add(dados);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.print("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversoDados.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversoDados.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        List<Serie> series = new ArrayList<>();
        series = dadosSerieList.stream()
                        .map(d -> new Serie(d))
                        .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
