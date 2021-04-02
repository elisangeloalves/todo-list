CREATE database IF NOT EXISTS todo;

USE todo;

CREATE TABLE IF NOT EXISTS task (
id bigint not null,
task_name varchar(100) not null,
status varchar(10) not null,
primary key (id)
);

INSERT INTO task(task_name, status) VALUES('Lavar as louças do almoço', 'completed');
INSERT INTO task(task_name, status) VALUES('Pagar as contas atrasadas', 'pending');
INSERT INTO task(task_name, status) VALUES('Fazer projeto para o teste técnico', 'completed');
INSERT INTO task(task_name, status) VALUES('Entregar os projetos acadêmicos em atraso', 'pending');
INSERT INTO task(task_name, status) VALUES('Estudar para a prova', 'completed');

