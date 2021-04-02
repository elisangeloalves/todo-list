package br.com.elisangelo.todoList.controller;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import br.com.elisangelo.todoList.controller.form.UpdateRequest;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;
import br.com.elisangelo.todoList.repository.TaskRepository;
import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/todo")
@Timed
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
			return TaskResponse.convertedList(repository.findAll());
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
			return ResponseEntity.ok("{\n\tmessage: Tarefa deletada com sucesso\n} ");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\n\tmessage: Tarefa não encontrada\n} ");
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody @Valid UpdateRequest request) {

		Optional<Task> optionalTask = repository.findById(id);
		if (optionalTask.isPresent()) {
			optionalTask.get().setStatus(request.getStatus());
			return ResponseEntity.ok(new TaskResponse(optionalTask.get()));
		}
		return  ResponseEntity.notFound().build();

	}

}

