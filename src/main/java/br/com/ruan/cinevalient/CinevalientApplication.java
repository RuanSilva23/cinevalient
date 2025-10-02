package br.com.ruan.cinevalient;

import br.com.ruan.cinevalient.model.DadosSerie;
import br.com.ruan.cinevalient.service.ConsumoAPI;
import br.com.ruan.cinevalient.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinevalientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CinevalientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=Lucifer&apikey=7a9c54d5");
		System.out.println(json);

		//json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);
        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
	}
}
