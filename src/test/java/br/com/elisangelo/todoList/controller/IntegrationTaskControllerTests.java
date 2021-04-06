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
import org.junit.jupiter.api.BeforeEach;
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
import br.com.elisangelo.todoList.controller.form.TaskUpdate;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;
import br.com.elisangelo.todoList.repository.TaskRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = MyTodoList.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class IntegrationTaskControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@PersistenceContext
	EntityManager manager;

	@Autowired
	TaskRepository repository;

	@Autowired
	TaskController controller = new TaskController();

	@BeforeEach
	@Transactional
	void beforeEachTest() {
		dropTable();
		createTodoList(null);
	}

	@AfterEach
	@Transactional
	void afterEachTest() {
		dropTable();
	}

	@Test
	@DisplayName(value = "Deve buscar por uma tarefa específica no banco atraves do Id")
	void testShouldListOneTask() throws Exception {
		TaskResponse foundTask = readingFromTheDB("organizar festa de formatura");
		Long id = foundTask.getId();
		String taskName = foundTask.getName();
		String status = foundTask.getStatus().toString();

		mockMvc.perform(get("/todo/{id}", id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(taskName))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status));
	}

	@Test
	@DisplayName(value = "Não deve trazer uma tarefa que não existe no banco atraves do Id")
	void testItShouldNotListATaskNotSaveInDB() throws Exception {
		Long TaskId = controller.listAllTasks(null, null).get(3).getId() + 1L;
		mockMvc.perform(get("/todo/{id}", TaskId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotHaveJsonPath());
	}

	@Test
	@DisplayName(value = "Deve permitir adicionar uma tarefa sem status e retornar uma tarefa com status \"pending\"")
	void testShouldAddATaskWithStatus() throws Exception {
		TaskRequest request = new TaskRequest("cuidar dos afazeres de casa", null);
		String nameTask = request.getName();
		String status = Status.pending.toString();

		mockMvc
				.perform(post("/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(jsonAsString(request)))
				.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(nameTask))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status));
	}

	@Test
	@DisplayName(value = "Deve permitir adicionar uma tarefa com nome e status (completed) e retornar tarefa com status completed")
	void testShouldAddATaskWithoutStatus() throws Exception {
		TaskRequest request = new TaskRequest("jogar o lixo fora", Status.completed);
		String nameTask = request.getName();
		String status = request.getStatus().toString();

		mockMvc
				.perform(post("/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(jsonAsString(request)))
				.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.name").value(nameTask))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status));

	}

	@Test
	@DisplayName(value = "Não deve permitir adicionar uma tarefa sem nome ou só com status")
	void testItShouldNotAddTaskWithoutName() throws Exception {
		TaskRequest request = new TaskRequest(null, Status.completed);
		mockMvc
				.perform(post("/todo").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(jsonAsString(request)))
				.andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.status").doesNotHaveJsonPath());
	}

	@Test
	@DisplayName(value = "Deve deletar uma tarefa do banco pelo id")
	void testShouldDeleteATask() throws Exception {
		Long validId = controller.listAllTasks(null, null).get(3).getId();
		mockMvc.perform(delete("/todo/{id}", validId)).andExpect(status().isAccepted());
	}

	@Test
	@DisplayName(value = "Não deve ser possível deletar uma tarefa de um id inexistente")
	void testItShouldNotDeleteTaskWithNoId() throws Exception {
		Long notValidId = controller.listAllTasks(null, null).get(3).getId() + 1L;

		mockMvc.perform(get("/todo/{id}", notValidId)).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName(value = "Deve ser possível atualizar o status de uma tarefa pelo id")
	void testShouldUpdateTheStatusOfATask() throws Exception {
		TaskResponse taskTobeUpdated = controller.listAllTasks(null, null).get(1);
		Long id = taskTobeUpdated.getId();
		String previousTaskName = taskTobeUpdated.getName();
		
		TaskUpdate request = new TaskUpdate(previousTaskName,Status.completed);
		String status = request.getStatus().toString();
		mockMvc
				.perform(put("/todo/{id}", id)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(jsonAsString(request)))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(previousTaskName));
	}


	@Test
	@DisplayName(value = "Deve ser possível atualizar somente o status de uma tarefa pelo id")
	void testitShouldNotBePossibleUpdateTheNameOfATask() throws Exception {
		TaskResponse taskTobeUpdated = controller.listAllTasks(null, null).get(3);
		Long id = taskTobeUpdated.getId();
		String previousTaskName = taskTobeUpdated.getName();
		
		TaskUpdate request = new TaskUpdate("Não deveria ter nada aqui", Status.completed);
		String status = request.getStatus().toString();

		mockMvc
				.perform(put("/todo/{id}", id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(jsonAsString(request)))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(previousTaskName))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status));
			}

	private void createTodoList(Task task) {
		List<Task> todoList = new ArrayList<>();

		if (task != null) {
			todoList.add(task);
		} else {
			final Task task1 = new Task("Lavar as louças do almoço", Status.completed);
			final Task task2 = new Task("organizar festa de formatura", Status.pending);
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

	private TaskResponse readingFromTheDB(String nameTask) {
		return controller.listAllTasks(null, nameTask).get(0);
	}

}
