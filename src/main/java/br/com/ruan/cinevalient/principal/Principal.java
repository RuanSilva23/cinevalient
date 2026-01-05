package br.com.ruan.cinevalient.principal;

import br.com.ruan.cinevalient.model.*;
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

    private Optional<Serie> serieBusca;



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
                 7 - Buscar por categoria
                 8 - Buscar por temporadas e avaliação
                 9 - Buscar por trecho
                 10 - Top episodio Por Série
                 11 - Buscado episodio por Data de Lançamento
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


                    case BUSCAR_SERIE_CATEGORIA:
                        buscarSeriePorCategoria();
                        break;

                    case BUSCAR_SERIE_POR_TEMPORADA_E_AVALIACAO:
                        buscarSeriePorTemporadaEAvaliacao();
                        break;

                    case BUSCAR_SERIE_TRECHO:
                        buscarEpisodioPorTrecho();
                        break;

                    case TOP_EPISODIO_SERIE:
                        topEpisodioPorSerie();
                        break;

                    case BUSCA_EPISODIO_DATA_LANCAMENTO:
                        buscaEpisodioPorDataDeLancamento();
                        break;

                    case SAIR:
                        System.out.println("Saindo...");
                        break;

                }
            }while (opcao != 0);
    }

    private void buscarSerieWeb() {
        System.out.print("Digite a serie a ser buscada: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Série já buscada anteriormente: " + serieBusca.get().getTitulo());

        }else {
            DadosSerie dados = getDadosSerie(nomeSerie);
            Serie serie = new Serie(dados);
            repository.save(serie);
            System.out.println("Série salva com sucesso no banco de dados");
            System.out.println(dados);
        }
    }

    private DadosSerie getDadosSerie(String nomeSerie) {
        System.out.println("Buscando série.....");
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversoDados.obterDados(json, DadosSerie.class);
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
       serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());

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

    private void buscarSeriePorCategoria(){
        System.out.print("Qual categoria/gênero será buscada? ");
        var nomeCategoria = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeCategoria);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Series da categoria " + categoria + ":");
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePorTemporadaEAvaliacao(){
        System.out.print("Qual número máximo de temporadas? ");
        var temporadas = scanner.nextInt();
        System.out.print("Qual o mínimo de avaliação? ");
        double minAvaliacao = scanner.nextInt();
        List<Serie> seriesPorTemporadaEAvaliacao = repository.seriesPorTemporadaEAvaliacao(temporadas, minAvaliacao);
        seriesPorTemporadaEAvaliacao.forEach(s ->
                System.out.printf("Série: %s - Avaliação: %s - Temp: %s\n", s.getTitulo(), s.getAvaliacao(), s.getTotalTemporadas()));

    }

    private void buscarEpisodioPorTrecho(){
        System.out.print("Qual o nome do episódio para busca? ");
        var trechoEpisodio = scanner.nextLine();
        List<Episodio> episodiosPorTrecho = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosPorTrecho.forEach(e->
                System.out.printf("Série: %s - Temporada: %s - Episódio %s - %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumero(), e.getTitulo()));
    }

    private void topEpisodioPorSerie(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episodio %s - %s - Avaliação: %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumero(), e.getTitulo(), e.getAvaliacao()));
        }

    }

    private void buscaEpisodioPorDataDeLancamento() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            System.out.println("Qual data limite?");
            var dataLancamento = scanner.nextInt();
            scanner.nextLine();
            List<Episodio> episodiosPorData = repository.episodiosPorData(serie, dataLancamento);
            episodiosPorData.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episodio %s - Avaliação: %s - Data de Lançamento: %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumero(), e.getAvaliacao(), e.getDataLancamento()));

        }
    }

}
