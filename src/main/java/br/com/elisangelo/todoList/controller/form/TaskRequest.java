package br.com.elisangelo.todoList.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.elisangelo.todoList.config.validation.UniqueName;
import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;

public class TaskRequest {

	
	@UniqueName(domainClass = Task.class, fieldName = "name")
	@NotBlank @Size(min=5, max = 100)
	private String name;
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

}
