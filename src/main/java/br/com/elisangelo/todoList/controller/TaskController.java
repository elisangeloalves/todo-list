package br.com.elisangelo.todoList.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.elisangelo.todoList.controller.Dto.TaskResponse;
import br.com.elisangelo.todoList.controller.form.TaskRequest;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;
import br.com.elisangelo.todoList.repository.TaskRepository;

@RestController
@RequestMapping("/todo")
public class TaskController {

	@Autowired
	TaskRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity<TaskResponse> addTask(@RequestBody @Valid TaskRequest request, UriComponentsBuilder builder) {
		Task newTask = request.toEntity();
		repository.save(newTask);
		URI uri = builder.path("/todo/{id}").buildAndExpand(newTask.getId()).toUri();
		return ResponseEntity.created(uri).body(new TaskResponse(newTask));
	}

	@GetMapping
	public List<TaskResponse> listAllTasks(@Param(value = "status") Status status, @Param(value = "name") String name) {
		if (status != null) {
			List<Task> selectedTask = repository.findByStatus(status);
			return TaskResponse.convertedList(selectedTask);
		}
		if (name != null) {
			List<Task> selectedTask = repository.findByName(name);
			return TaskResponse.convertedList(selectedTask);
		} else {
			List<Task> tasks = repository.findAll();
			return TaskResponse.convertedList(tasks);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskResponse> listOne(@PathVariable Long id) {
		Optional<Task> optional = repository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(new TaskResponse(optional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<String> deleteTask(@PathVariable Long id) {
		Optional<Task> optional = repository.findById(id);
		if (optional.isPresent()) {
			repository.deleteById(id);
			return ResponseEntity.ok("{ message: Tarefa deletada com sucesso } ");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ message: Tarefa não encontrada } ");
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest request) {
		Optional<Task> optional = repository.findById(id);
		if (optional.isPresent()) {
			Task editedTask = request.update(optional.get());
			return ResponseEntity.ok(new TaskResponse(editedTask));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ message: Tarefa não encontrada } ");
	}

}

//Projeto: TODO-LIST API (API para armazenamento/leitura de tarefas) 
//
//- Criar uma RESTFUL API simples (em sua linguagem preferida) que armazene e atualize tarefas (TODO LIST API). (ex: GET, PUT, POST, DELETE /todo)
//
// Informações sobre a API:
//
//  - Toda tarefa deve possuir um status (pending ou completed)
//
//  - A API deve persistir os dados em um banco de dados
//
//  - A API deve disponibilizar uma rota para listagem das tarefas e seu status (GET /todo)
//
//  - A API deve fornecer uma rota para validar o funcionamento de seus componentes (GET /healthcheck)
//
//  - A API deve fornecer uma rota com indicadores de performance da API (ex: volume de requisições atendidas, tempo médio de serviço em milisegundos, etc) (GET /metrics)
//
//  - Não é necessário desenvolver o Frontend para input dos dados na API.
