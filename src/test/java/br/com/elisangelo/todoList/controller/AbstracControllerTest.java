package br.com.elisangelo.todoList.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstracControllerTest {

	public static String jsonAsString(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
