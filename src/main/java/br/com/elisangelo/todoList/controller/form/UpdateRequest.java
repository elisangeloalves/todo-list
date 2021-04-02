package br.com.elisangelo.todoList.controller.form;

import javax.validation.constraints.NotNull;

import br.com.elisangelo.todoList.model.Status;

public class UpdateRequest {

	@NotNull
	private Status status;
	
	public Status getStatus() {
		return this.status;
	}

}
