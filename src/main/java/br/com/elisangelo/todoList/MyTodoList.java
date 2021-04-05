package br.com.elisangelo.todoList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
@SpringBootApplication
public class MyTodoList {

	public static void main(String[] args) {
		SpringApplication.run(MyTodoList.class, args);
	}
}
