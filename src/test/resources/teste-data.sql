CREATE database IF NOT EXISTS todo_test;

USE todo_test;

CREATE TABLE IF NOT EXISTS task (
id bigint not null,
task_name varchar(100) not null,
status varchar(10) not null,
primary key (id)
);

--INSERT INTO task(task_name, status) VALUES('Investir em cursos e certificacoes', 'pending');
--INSERT INTO task(task_name, status) VALUES('Iniciar transição de carreira', 'completed');
