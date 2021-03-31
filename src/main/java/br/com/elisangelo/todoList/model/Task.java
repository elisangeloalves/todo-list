package br.com.elisangelo.todoList.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Task {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank 
	private String name;
	@NotNull @Enumerated(EnumType.STRING)
	private Status status;
	
	@Deprecated
	public Task() {
	};
	
	public Task(@NotBlank String name, @Valid @NotNull Status status) {
		this.name = name;
		this.status = status;
	};
	
	public Long getId() {
		return id;
	};
	
	public String getName() {
		return name;
	};
	
	public Status getStatus() {
		return status;
	}

	public void setName(@NotBlank String editedName) {
		this.name = editedName;
		
	}

	public void setStatus(@NotNull @Valid Status editedStatus) {
		this.status = editedStatus;
	}

}
