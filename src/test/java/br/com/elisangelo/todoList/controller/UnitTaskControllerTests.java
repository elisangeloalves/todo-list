package br.com.elisangelo.todoList.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import br.com.elisangelo.todoList.MyTodoList;
import br.com.elisangelo.todoList.controller.dto.TaskResponse;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;
import br.com.elisangelo.todoList.repository.TaskRepository;

@ActiveProfiles("test")
@SpringBootTest(classes = MyTodoList.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class UnitTaskControllerTests {

	
	@Autowired
	TaskRepository repository;
	
	@Autowired
	TaskController controller = new TaskController();
	
	@BeforeEach
	@Transactional
	void beforeAllTest() {
		repository.deleteAll();
		createTodoList(null);
	}
	
	@AfterEach
	@Transactional
	void afterEachTest() {
		dropTable();
	}
	
	@Test
	@DisplayName(value = "Deve listar todas as tarefas existentes no banco")
	void shouldListAllTasks() {
		List<TaskResponse> result = controller.listAllTasks(null, null);
		
		assertFalse(result.isEmpty());
		assertEquals(4, result.size());
		
		Task task5 = new Task("Dormir mais cedo", null);
		Task task6 = new Task("levar a gatinha pra passear", Status.completed);
		createTodoList(task5);
		createTodoList(task6);
		
		result = controller.listAllTasks(null, null);
		assertEquals(6, result.size());
	}
	
	@Test
	@DisplayName(value = "Deve buscar tarefa pelo nome")
	void shouldFindTaskByName() {
		String taskName = "Lavar as louças do almoço";
		List<TaskResponse> result = controller.listAllTasks(null, taskName);
		
		assertTrue(result.size() <= 1, "Algo estranho aconteceu violando a regra de unicidade no banco de dados");
		assertNotNull(result);
		assertEquals(taskName, result.get(0).getName());
	}

	@Test
	@DisplayName(value = "Não deveria trazer uma tarefa que não existe no banco")
	void shouldNotFindATaskByNameThatDoesNotExist() {
		String taskName = "Essa tarefa não tem no banco";
		List<TaskResponse> result = controller.listAllTasks(null, taskName);
		assertEquals(0, result.size());
	}
	
	@Test
	@DisplayName(value = "Deve buscar todas as tarefas com status \"pending\"")
	void shouldFindTaskByStatusPending() {
		List<TaskResponse> result = controller.listAllTasks(Status.pending, null);
		
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		result.forEach(c -> assertEquals(Status.pending, c.getStatus()));
	}

	@Test
	@DisplayName(value = "Deve buscar todas as tarefas com status \"completed\"")
	void shouldFindTaskByStatusCompleted() {
		List<TaskResponse> result = controller.listAllTasks(Status.completed, null);
		
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		
		Task task = new Task("Dormir mais cedo", Status.completed);
		createTodoList(task);
		result = controller.listAllTasks(Status.completed, null);
		
		assertEquals(3, result.size());
		result.forEach(c -> assertEquals(Status.completed, c.getStatus()));
	}
	
	private void createTodoList(Task task) {
		List<Task> todoList = new ArrayList<>();
		
		if (task != null) {
			todoList.add(task);
		}
		else {
			final Task task1 = new Task("Lavar as louças do almoço", Status.completed);
			final Task task2 = new Task("Pagar as contas atrasadas", Status.pending);
			final Task task3 = new Task("Fazer projeto para o teste", Status.completed);
			final Task task4 = new Task("Entregar os projetos acadêmicos em atraso", Status.pending);
			
			todoList.add(task1);
			todoList.add(task2);
			todoList.add(task3);
			todoList.add(task4);
		}
		repository.saveAll(todoList);
	}
	
	private void dropTable() {
		repository.deleteAll();
	}
}
