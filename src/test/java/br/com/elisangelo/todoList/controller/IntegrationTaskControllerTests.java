package br.com.elisangelo.todoList.controller;

import static br.com.elisangelo.todoList.controller.AbstracControllerTest.jsonAsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.elisangelo.todoList.MyTodoList;
import br.com.elisangelo.todoList.controller.dto.TaskResponse;
import br.com.elisangelo.todoList.controller.form.TaskRequest;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;
import br.com.elisangelo.todoList.repository.TaskRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = MyTodoList.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class IntegrationTaskControllerTests {

	private Task task1 = new Task("organizar a rotina de estudos", Status.completed);
	private Task task2 = new Task("organizar festa de formatura", null);

	@Autowired
	private MockMvc mockMvc;

	@PersistenceContext
	EntityManager manager;

	@Autowired
	TaskRepository repository;

	@Autowired
	TaskController controller = new TaskController();

	@AfterEach
	@Transactional
	void afterEachTest() {
		dropTable();
	}

	@Test
	@DisplayName(value = "Deve buscar por uma tarefa específica no banco pelo Id")
	void testShouldListOneTask() throws Exception {
		List<Task> todoList = insertTodoList();

		mockMvc.perform(get("/todo/{id}", todoList.get(0).getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(jsonAsString(task1))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(todoList.get(0).getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(task1.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(task1.getStatus().toString()));

		mockMvc.perform(get("/todo/{id}", todoList.get(1).getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(jsonAsString(task2))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(todoList.get(1).getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(task2.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(task2.getStatus().toString()));
	}

	@Test
	@DisplayName(value = "Deve permitir adicinar uma tarefa sem status e retornar uma tarefa com status (pending)")
	void testShouldAddATaskWithStatus() throws Exception {
		TaskRequest request = new TaskRequest("cuidar dos afazeres de casa");

		mockMvc.perform(post("/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(jsonAsString(request))).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("cuidar dos afazeres de casa"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("pending"));

	}

	@Test
	@DisplayName(value = "Deve permitir adicionar uma tarefa com nome e status (completed) e retornar tarefa com status completed")
	void testShouldAddATaskWithoutStatus() throws Exception {
		TaskRequest request = new TaskRequest("jogar o lixo fora", Status.completed);

		mockMvc.perform(post("/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(jsonAsString(request))).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("jogar o lixo fora"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("completed"));

	}

	@Test
	@DisplayName(value = "Deve deletar uma tarefa do banco pelo id")
	void testShouldDeleteATask() throws Exception {
		insertTodoList();
		List<TaskResponse> task = readingFromTheDB(task1.getName());

		mockMvc.perform(delete("/todo/{id}", task.get(0).getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted());

		mockMvc.perform(get("/todo/{id}", task.get(0).getId()).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName(value = "Deve atualizar o status de uma tarefa do banco pelo id")
	void testShouldUpdateATask() throws Exception {
		insertTodoList();
		List<TaskResponse> task = readingFromTheDB(task1.getName());
		
		// task("organizar a rotina de estudos", Status.completed);
		Long idTobeUpdated = task.get(0).getId();
		System.out.println(
				"name: " + task.get(0).getName() + "id: " + task.get(0).getId() + "status: " + task.get(0).getStatus());

		TaskRequest request = new TaskRequest(null, Status.pending);

		mockMvc.perform(put("/todo/{id}", idTobeUpdated).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(jsonAsString(request))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(idTobeUpdated))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(request.getStatus().toString()));
	}

	private List<Task> insertTodoList() {
		List<Task> todoList = new ArrayList<>();
		todoList.add(task1);
		todoList.add(task2);
		return repository.saveAll(todoList);
	}

	private void dropTable() {
		repository.deleteAll();
		String query = "DROP TABLE " + ":tableName";
		manager.createNativeQuery(query).setParameter("tableName", "task");
	}

	private List<TaskResponse> readingFromTheDB(String nameTask) {
		List<br.com.elisangelo.todoList.controller.dto.TaskResponse> result = controller.listAllTasks(null, nameTask);
		return result;
	}

}
