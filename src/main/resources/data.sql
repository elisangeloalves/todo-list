CREATE database IF NOT EXISTS todo;

USE todo;

CREATE TABLE IF NOT EXISTS task (
id bigint not null,
task_name varchar(100) not null,
status varchar(10) not null,
primary key (id)
);
--
