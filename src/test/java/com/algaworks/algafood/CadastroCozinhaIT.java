package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

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
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ClasseUtilitaria;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
//	@Autowired
//	private Flyway flyway;
	
	private static final int COZINHA_ID_INEXISTENTE = 100;

	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;
	
	@Before
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.basePath = "/cozinhas";
		RestAssured.port = port;
		
		//flyway.migrate(); //agora usar DatabaseCleaner
		databaseCleaner.clearTables();
		prepararDados();
		jsonCorretoCozinhaChinesa = ClasseUtilitaria.recebeJson("/json/correto/cozinha-chinesa.json");
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		//RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		given()
			//.basePath("/cozinhas")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConterTotalDeCozinhas_QuandoConsultarCozinhas() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		given()
			//.basePath("/cozinhas")
			//.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeCozinhasCadastradas));
			
	}
	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			//.body("{\"nome\": \"Imalaia\"}")
			.body(jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)			
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
					
	}
	
	@Test
	public void deveRetornarRespostaEEstatosCorretos_quandoConsultarCozinhaExistente() {
		
		given()
		.pathParam("cozinhaId",quantidadeCozinhasCadastradas)
		.accept(ContentType.JSON)
	.when()
		.get("/{cozinhaId}")
	.then()
		.statusCode(HttpStatus.OK.value())
		.body("nome", equalTo(cozinhaAmericana.getNome()));		
	}
	
	
	@Test
	public void deveRetornarResposta404_quandoConsultarCozinhaInexistente() {
		
		given()
		.pathParam("cozinhaId",COZINHA_ID_INEXISTENTE)
		.accept(ContentType.JSON)
	.when()
		.get("/{cozinhaId}")
	.then()
		.statusCode(HttpStatus.NOT_FOUND.value());			
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandeza");		
		cozinhaRepository.save(cozinha1);
		
		Cozinha cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");		
		cozinhaRepository.save(cozinhaAmericana);
		
		quantidadeCozinhasCadastradas = (int)cozinhaRepository.count();
		
	}
	

	
//	@Autowired
//	private CadastroCozinhaService cadastroCozinha;
//	
//	@Test
//	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
//		//cenario
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome("Cubana");
//		//ação
//		novaCozinha = cadastroCozinha.salvar(novaCozinha);
//		//teste
//		assertThat(novaCozinha).isNotNull();
//		assertThat(novaCozinha.getId()).isNotNull();
//		assertEquals("cubana", novaCozinha.getNome().toLowerCase());						
//	}
//	
//	@Test(expected = ConstraintViolationException.class)
//	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
//		//cenario
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome(null);
//		//ação
//		novaCozinha = cadastroCozinha.salvar(novaCozinha);				
//	}
//	
//	@Test(expected = EntidadeEmUsoException.class)
//	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
//		long id = 1L;
//		cadastroCozinha.excluir(id);
//	}
//
//	@Test(expected = CozinhaNaoEncontradaException.class)
//	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
//		cadastroCozinha.excluir(100L);
//	}	
	
}
