package br.com.elisangelo.todoList.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;

public class TaskResponse {

	private Long id;
	private String name;
	private Status status;

	public TaskResponse(Task task) {
		this.id = task.getId();
		this.name = task.getName();
		this.status = task.getStatus();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Status getStatus() {
		return status;
	}

	public static List<TaskResponse> convertedList(List<Task> selectedTask) {
		return selectedTask.stream().map(TaskResponse::new).collect(Collectors.toList());
	}

}
