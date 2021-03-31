package br.com.elisangelo.todoList.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.elisangelo.todoList.model.Status;
import br.com.elisangelo.todoList.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByStatus(Status status);

	List<Task> findByName(String name);
}
