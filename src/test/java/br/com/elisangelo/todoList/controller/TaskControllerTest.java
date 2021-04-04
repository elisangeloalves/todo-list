package br.com.elisangelo.todoList.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.util.Assert;

import br.com.elisangelo.todoList.controller.Dto.TaskResponse;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;
import br.com.elisangelo.todoList.repository.TaskRepository;

@SpringBootTest(properties = "test")
class TaskControllerTest {

	private Task task1 = new Task("Lavar as louças do almoço", Status.completed);
	private Task task2 = new Task("Pagar as contas atrasadas", Status.pending);
	private Task task3 = new Task("Fazer projeto para o teste", Status.completed);
	private Task task4 = new Task("Entregar os projetos acadêmicos em atraso", Status.pending);
	
	@Autowired
	TaskController controller =  new TaskController();

	@Autowired
	TaskRepository repository;
	
	@PersistenceContext
	EntityManager manager;
	
	@BeforeEach
	@Transactional
	void beforeAllTest() {
		createTodoList();
	}

	@AfterEach
	@Transactional
	void afterEachTest() {
	    dropTable();
	}

	
	@Test
	void shouldFindTaskByName() {
		
		List<TaskResponse> result = controller.listAllTasks(null, task1.getName());
		result.forEach(c -> System.out.println("teste 1 id: " + c.getId()+" "+ c.getName()));
		
		assertNotNull(result);
		assertTrue(result.size() <=1, "Algo estranho aconteceu violando a regra de unicidade no banco de dados");
		assertEquals(task1.getName(), result.get(0).getName());
	}
	
	@Test
	void shouldFindTaskByStatus() {
		
		List<TaskResponse> result =  controller.listAllTasks(Status.completed, null);
		
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		result.forEach(c -> assertEquals(Status.completed, c.getStatus()));
		
		Task task = new Task("Dormir mais cedo", null);
		repository.save(task);
		result =  controller.listAllTasks(Status.pending, null);
	
		assertFalse(result.isEmpty());
		assertEquals(3, result.size());
		result.forEach(c -> assertEquals(Status.pending, c.getStatus()));
	}
	
	@Test
	void shouldListAllTasks() {
		
		List<TaskResponse> result =  controller.listAllTasks(null, null);
		
		assertFalse(result.isEmpty());
		assertEquals(4, result.size());
		
		Task task5 = new Task("Dormir mais cedo", null);
		Task task6 = new Task("levar a gatinha pra passear", Status.completed);

		repository.save(task5);
		repository.save(task6);

		result = controller.listAllTasks(null, null);
		assertEquals(6, result.size());	
	}

	private void createTodoList() {
		List<Task> todoList = new ArrayList<>();
		System.out.println("before each tests: ");
		
		todoList.add(task1);
		todoList.add(task2);
		todoList.add(task3);
		todoList.add(task4);
		
		repository.saveAll(todoList);
	}

	private void dropTable() {
		repository.deleteAll();
		String query = "DROP TABLE "+":tableName";
		manager.createNativeQuery(query)
		.setParameter("tableName", "task");
		System.out.println("after each test: "+query);
	}
	
}
