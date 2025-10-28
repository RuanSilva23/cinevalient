package br.com.ruan.cinevalient;

import br.com.ruan.cinevalient.principal.Principal;
import br.com.ruan.cinevalient.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinevalientApplication implements CommandLineRunner {
    @Autowired
    private SerieRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(CinevalientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        Principal principal = new Principal(repository);
        principal.exibeMenu();

	}
}
