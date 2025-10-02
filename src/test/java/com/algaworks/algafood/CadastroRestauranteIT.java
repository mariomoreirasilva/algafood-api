package com.algaworks.algafood;

import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {
	
	 @LocalServerPort
	    private int port;
	    
	    @Autowired
	    private DatabaseCleaner databaseCleaner;
	    
	    @Autowired
	    private CozinhaRepository cozinhaRepository;
	    
	    @Autowired
	    private RestauranteRepository restauranteRepository;
	    
	    private Restaurante burgerTopRestaurante;
	    
	    private int qtdRestaurantes;
	    
	@Before
	public void setup() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.basePath = "/restaurantes";
		RestAssured.port = port;
		databaseCleaner.clearTables();
		prepararDados();
		
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
		
		RestAssured.given()
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
		
	}
		
	@Test
	public void deveRetornarQuantidadeDeRestaurantes_QuandoConsultarRestaurantes() {
		
		RestAssured.given()
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()	
			.body("", hasSize(qtdRestaurantes));
		
	}
	
	private void prepararDados() {
	        Cozinha cozinhaBrasileira = new Cozinha();
	        cozinhaBrasileira.setNome("Brasileira1");
	        cozinhaRepository.save(cozinhaBrasileira);

	        Cozinha cozinhaAmericana = new Cozinha();
	        cozinhaAmericana.setNome("Americana1");
	        cozinhaRepository.save(cozinhaAmericana);
	        
	        burgerTopRestaurante = new Restaurante();
	        burgerTopRestaurante.setNome("Burger Top");
	        burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
	        burgerTopRestaurante.setCozinha(cozinhaAmericana);
	        restauranteRepository.save(burgerTopRestaurante);
	        
	        Restaurante comidaMineiraRestaurante = new Restaurante();
	        comidaMineiraRestaurante.setNome("Comida Mineira");
	        comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
	        comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
	        restauranteRepository.save(comidaMineiraRestaurante);
	        
	        qtdRestaurantes = (int)restauranteRepository.count();
	    }            

}
