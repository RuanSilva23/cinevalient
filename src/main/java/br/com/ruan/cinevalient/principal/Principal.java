package br.com.ruan.cinevalient.principal;

import br.com.ruan.cinevalient.model.DadosEpisodios;
import br.com.ruan.cinevalient.model.DadosSerie;
import br.com.ruan.cinevalient.model.DadosTemporada;
import br.com.ruan.cinevalient.model.Episodio;
import br.com.ruan.cinevalient.service.ConsumoAPI;
import br.com.ruan.cinevalient.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=7a9c54d5";

    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        System.out.print("Digite o nome da série para a buscar: ");
        var nomeSerie = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas =new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodios> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e-> System.out.println(e.titulo())));

        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                        .flatMap(t -> t.episodios().stream())
                        .collect(Collectors.toList());


        System.out.println("Os melhores são: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .limit(4)
                .map(e -> e.titulo().toUpperCase())
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                                .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano: ");
        var ano = scanner.nextInt();
        scanner.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1 ,1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                        .filter(e ->e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                                .forEach(e -> System.out.println(
                                        "Temporada: " + e.getTemporada() +
                                        "\nEpisódio: " + e.getTitulo() +
                                        "\nData Lançamento : " + e.getDataLancamento().format(formatter)
                                ));

        scanner.close();

    }
}
