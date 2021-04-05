DROP database IF EXISTS todo;

CREATE database todo;

USE todo;

CREATE TABLE task (
id bigint not null auto_increment,
task_name varchar(100) not null,
status varchar(10) not null,
primary key (id)
);
