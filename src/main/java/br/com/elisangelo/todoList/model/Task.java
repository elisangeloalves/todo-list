package br.com.elisangelo.todoList.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(nullable=false, name="task_name", length=100)
	private String name;
	@NotNull @Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.pending;

	@Deprecated
	public Task() {
	};

	public Task(@NotBlank String name, Status status) {
		this.name = name;
		if (status != null) {
		this.status = status;
		}
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

	public void setStatus(@NotNull @Valid Status editedStatus) {
		this.status = editedStatus;
	}

}
