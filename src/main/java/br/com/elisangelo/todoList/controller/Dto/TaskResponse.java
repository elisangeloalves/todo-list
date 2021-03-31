package br.com.elisangelo.todoList.controller.Dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.elisangelo.todoList.model.Task;

public class TaskResponse {

	private Long id;
	private String name;
	private String status;

	public TaskResponse(Task task) {
		this.id = task.getId();
		this.name = task.getName();
		this.status = task.getStatus().toString();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public static List<TaskResponse> convertedList(List<Task> tasks) {
		return tasks.stream().map(TaskResponse::new).collect(Collectors.toList());
	}

}
