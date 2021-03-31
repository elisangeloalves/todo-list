package br.com.elisangelo.todoList.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;

public class TaskRequest {

	@NotBlank
	private String name;
	@NotNull
	private Status status;

	public String getName() {
		return name;
	}

	public Status getStatus() {
		return status;
	}

	public Task toEntity() {
		return new Task(this.name, this.status);
	}

	public Task update(Task task) {
		task.setName(this.name);
		task.setStatus(this.status);
		return task;
	}

}
