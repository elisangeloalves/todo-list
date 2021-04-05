package br.com.elisangelo.todoList.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

	private Task task1 = new Task("Lavar as louças do almoço", Status.completed);
	private Task task2 = new Task("Pagar as contas atrasadas", Status.pending);
	private Task task3 = new Task("Fazer projeto para o teste", Status.completed);
	private Task task4 = new Task("Entregar os projetos acadêmicos em atraso", Status.pending);

	@Autowired
	TaskRepository repository;

	@Autowired
	TaskController controller = new TaskController();

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
	@DisplayName(value = "Deve listar todas as tarefas existentes no banco")
	void shouldListAllTasks() {
		List<TaskResponse> result = controller.listAllTasks(null, null);

		assertFalse(result.isEmpty());
		assertEquals(4, result.size());

		Task task5 = new Task("Dormir mais cedo", null);
		Task task6 = new Task("levar a gatinha pra passear", Status.completed);
		repository.save(task5);
		repository.save(task6);

		result = controller.listAllTasks(null, null);
		assertEquals(6, result.size());
	}

	@Test
	@DisplayName(value = "Deve buscar tarefa pelo nome")
	void shouldFindTaskByName() {
		List<TaskResponse> result = controller.listAllTasks(null, task1.getName());

		assertNotNull(result);
		assertTrue(result.size() <= 1, "Algo estranho aconteceu violando a regra de unicidade no banco de dados");
		assertEquals(task1.getName(), result.get(0).getName());
	}

	@Test
	@DisplayName(value = "Deve buscar todas as tarefas por um determinado status")
	void shouldFindTaskByStatus() {
		List<TaskResponse> result = controller.listAllTasks(Status.completed, null);

		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
		result.forEach(c -> assertEquals(Status.completed, c.getStatus()));

		Task task = new Task("Dormir mais cedo", null);
		repository.save(task);
		result = controller.listAllTasks(Status.pending, null);

		assertFalse(result.isEmpty());
		assertEquals(3, result.size());
		result.forEach(c -> assertEquals(Status.pending, c.getStatus()));
	}

	private void createTodoList() {
		List<Task> todoList = new ArrayList<>();
		todoList.add(task1);
		todoList.add(task2);
		todoList.add(task3);
		todoList.add(task4);
		repository.saveAll(todoList);
	}

	private void dropTable() {
		repository.deleteAll();
		String query = "DROP TABLE " + ":tableName";
		manager.createNativeQuery(query).setParameter("tableName", "task");
	}
}
