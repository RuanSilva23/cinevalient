package br.com.ruan.cinevalient.principal;

import br.com.ruan.cinevalient.model.DadosSerie;
import br.com.ruan.cinevalient.model.DadosTemporada;
import br.com.ruan.cinevalient.model.Episodio;
import br.com.ruan.cinevalient.model.Serie;
import br.com.ruan.cinevalient.repository.SerieRepository;
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

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repository){
        this.repository = repository;
    }

    public void exibeMenu() {
        int opcao;
        var menu = """
                Opcões:
                 1 - Buscar por séries
                 2 - Buscar por episódios
                 3 - Listar séries buscadas
                 4 - Buscar série por nome
                 5 - Buscar séries por ator
                 6 - Top 5 séries por avaliação
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

                    case BUSCAR_SERIE_POR_TITULO:
                        buscarSeriePorTitulo();
                        break;

                    case BUSCAR_SERIE_POR_ATOR:
                        buscarSeriePorAtor();
                        break;

                    case TOP_5_SERIES:
                        buscarTop5Serie();
                        break;


                    case SAIR:
                        System.out.println("Saindo...");
                        break;

                }
            }while (opcao != 0);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        //dadosSerieList.add(dados);
        Serie serie = new Serie(dados);
        repository.save(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.print("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversoDados.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        System.out.println("----------------");
        listarSeriesBuscadas();
        System.out.println("----------------");
        System.out.print("Digite a serie a ser buscada: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversoDados.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }



//        for (int i = 1; i ≤ dadosSerie.totalTemporadas(); i++) {
//            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
//            DadosTemporada dadosTemporada = conversoDados.obterDados(json, DadosTemporada.class);
//            temporadas.add(dadosTemporada);
//        }
//        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.print("Digite a serie a ser buscada: ");
        var nomeSerie = scanner.nextLine();
        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (serie.isPresent()) {
            System.out.println("Dados da série: " + serie.get());

        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriePorAtor(){
        System.out.print("Digite o nome do ator: ");
        var nomeAtor = scanner.nextLine();
        System.out.print("Digite a avaliação mínima: ");
        var avaliacaoMinima = scanner.nextDouble();
        scanner.nextLine();

        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacaoMinima);
        System.out.println("Séries em que " + nomeAtor + " atuou: ");

        seriesEncontradas.forEach(s ->
            System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao() + " - gênero: " + s.getGenero()));
    }

    private void buscarTop5Serie(){
        List<Serie> seriesTop = repository.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));

    }

}
