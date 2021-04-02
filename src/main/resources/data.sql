DROP database IF exists todo;
CREATE database todo;
USE todo;

DROP TABLE IF exists task;
CREATE TABLE task (
id bigint not null auto_increment,
task_name varchar(100) NOT NULL,
status varchar(10) NOT NULL,
primary key (id)
);

INSERT INTO task(task_name, status) VALUES('Lavar as louças do almoço', 'completed');
INSERT INTO task(task_name, status) VALUES('Pagar as contas atrasadas', 'pending');
INSERT INTO task(task_name, status) VALUES('Fazer projeto para o teste técnico', 'completed');
INSERT INTO task(task_name, status) VALUES('Entregar os projetos acadêmicos em atraso', 'pending');
INSERT INTO task(task_name, status) VALUES('Estudar para a prova', 'completed');

