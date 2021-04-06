package br.com.elisangelo.todoList.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.elisangelo.todoList.model.Status;

public class TaskUpdate {

	private String name;
	@NotNull
	private Status status;

	public String getName() {
		return name;
	}

	public Status getStatus() {
		return status;
	}

	public TaskUpdate(String justForTesting, @NotNull Status status) {
		this.name = justForTesting;
		this.status = status;
	}

}
