package com.algaworks.algafood.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

public class ClasseUtilitaria {
	
	public static String recebeJson(String nome) {
		
		try {
			InputStream stream = ClasseUtilitaria.class.getResourceAsStream(nome);
			return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}
