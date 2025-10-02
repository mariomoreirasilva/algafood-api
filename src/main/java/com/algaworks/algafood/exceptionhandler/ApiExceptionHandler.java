package com.algaworks.algafood.exceptionhandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String MSG_ERRO_GENERICO_USUARIO_FINAL = "Ocorrreu um erro inesperado no sistema. Tente novamente e se o problema "
			+ "persistir, entre em contato com o administrador do sistema";
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		//adiocionado biblioteca <groupId>org.apache.commons</groupId> no pom.xml
		//até a linha 33 serve para tratar campos do json inválidos, inteiro e digita string. criado o metodo abaixo handleInvalidFormatException
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			
			return handleInvalidFormatException( (InvalidFormatException) rootCause, headers, status, request);
			
		} else if (rootCause instanceof IgnoredPropertyException) {
			return handleCampoIgnoradoParaAtualizar( (IgnoredPropertyException) rootCause, headers, status, request);
			
		}else if (rootCause instanceof UnrecognizedPropertyException) {
			return handlePropriedadeInexistente((UnrecognizedPropertyException) rootCause, headers, status, request);
		}
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String datail = "Corpo da requisição inválido. Verifique a sintaxe.";
		
		Problem problem = createProblemBuilder(status, problemType, datail)
				.userMassage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	//se não cair em nenhuma tratada, faz a genérica
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleTrataGenerico(Exception ex, WebRequest request, HttpHeaders headers) {
		System.out.println("entrou");
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		//String datail = String.format("Erro de sistema. Pilha de chamada: '%s'", ex.getStackTrace().toString());
		String datail ="Erro de sistema.";
				
		Problem problem = createProblemBuilder(status, problemType, datail).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		//System.out.println("entrou");
		BindingResult bindingResult =  ex.getBindingResult();
		//vou deixar comentado como pegava antes de criar o aqruivo messages.properties
		List<Problem.Field> problemFields = bindingResult.getFieldErrors().stream()
				.map(fieldError -> {
					String menssagemCustomizada = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());//locale do sistema operacional
					
						return Problem.Field.builder()				
						.fildName(fieldError.getField())
						//.userMassege(fieldError.getDefaultMessage()).build(); //antes do arquivo. As mensagens na anotation do campo na classe
						.userMassege(menssagemCustomizada).build();
						})
						.collect(Collectors.toList());
		System.out.println(problemFields);
		ProblemType problemType = ProblemType.DADO_INVALIDO;
		status = HttpStatus.BAD_REQUEST;
		String datail = "Um ou mais campos estão inválidos. Faça o preencimento correto e tente novamente";

		Problem problema = createProblemBuilder(status, problemType, datail)
				.userMassage("Um ou mais campos estão inválidos. Faça o preencimento correto e tente novamente")
				.fields(problemFields)
				.build();
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso '%s', que tentou acessar não existe.", ex.getRequestURL());
				
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch(
					(MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
	
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMassage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	
	private ResponseEntity<Object> handlePropriedadeInexistente(UnrecognizedPropertyException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request){
		
		System.out.println("Entrou no método");
		String caminho = ex.getPath().stream()
				.map(x -> x.getFieldName())
				.collect(Collectors.joining("."));
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String datail = String.format("A propriedade '%s', não existe. "
				+ "Remova o campo informado no corpo da mensagem e tente atualizar novamente.",
				caminho);
				
		Problem problem = createProblemBuilder(status, problemType, datail)
				.userMassage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
		
	}
	
	private ResponseEntity<Object> handleCampoIgnoradoParaAtualizar(IgnoredPropertyException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request){
		
		String caminho = ex.getPath().stream()
				.map(x -> x.getFieldName())
				.collect(Collectors.joining("."));
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String datail = String.format("A propriedade '%s', está ignorada para atualização. "
				+ "Remova o campo informado no corpo da mensagem e tente atualizar novamente.",
				caminho);
				
		Problem problem = createProblemBuilder(status, problemType, datail).build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		//teste para ajudar a construir a mensagem
		//ex.getPath().forEach(x -> System.out.println(x.getFieldName()));
		String caminho = ex.getPath().stream()
				.map(x -> x.getFieldName())
				.collect(Collectors.joining("."));
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String datail = String.format("A propriedade '%s', recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo '%s' ",
				caminho, ex.getValue(), ex.getTargetType().getSimpleName());
				
		Problem problem = createProblemBuilder(status, problemType, datail)
				.userMassage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaExceptio(EntidadeNaoEncontradaException e
			, WebRequest request){
		//foi colocado a anotação Builder na classe.
		//se não colocar, teria que colocar a anotação setter ou data e fazer o construtor problema = new Problema()
		//prolema.setdataHora = .... problema.setmensagem = e.getMessage;
//		Problema problema = Problema.builder().dataHora(LocalDateTime.now())
//				.mensagem(e.getMessage()).build();
//		Problema problema = new Problema();
//		problema.setDataHora(LocalDateTime.now());
//		problema.setMensagem(e.getMessage());
		
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
		//agora usando o handleExceptionExtenal
		//return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
		
//		HttpStatus status = HttpStatus.NOT_FOUND;
//		String datail = e.getMessage();
//		
//		Problem problem = Problem.builder()
//				.status(status.value())
//				.type("https://www.desban.org.br/entidade-nao-encontrada")
//				.title("Entidade não encontrada")
//				.datail(datail)
//				.build();
//		
//		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String datail = e.getMessage();		
				
		Problem problem = createProblemBuilder(status, problemType, datail).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
		
 }
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.NEGOCIO_NAO_EXISTE;
		String detail = e.getMessage();		
		
		Problem problem = createProblemBuilder(status, problemType,detail).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
		
	}
	
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request){
		
		HttpStatus httpStatus = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String datail = e.getMessage();
		LocalDateTime dataHora = LocalDateTime.now();
		
		Problem problem = createProblemBuilder(httpStatus, problemType, datail).build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), httpStatus, request);
		
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(body == null) {
		   body = Problem.builder()
				   .title(status.getReasonPhrase())
				   .status(status.value())
				   .dataHora(LocalDateTime.now())
				   .build();
		}else if(body instanceof String) {
			body = Problem.builder()					
					.title((String) body)
					.status(status.value())
					.dataHora(LocalDateTime.now())
					.build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	//esse metodo para ajudar nas instanciações dos builders. 
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
			ProblemType problemType, String datail){
		
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.datail(datail)
				.dataHora(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
	}

}
