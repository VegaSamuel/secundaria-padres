#drop database escuela;
create database escuela_padres;
use escuela_padres;

create table padres(
	id int primary key auto_increment,
    nombre varchar(100) not null,
    email varchar(100) not null unique
);

create database escuela_alumnos;
use escuela_alumnos;

create table alumnos(
	id int primary key auto_increment,
    nombre varchar(100) not null,
    apellido varchar(100) not null,
    email varchar(100) not null unique,
    padre_id int not null
);

create database escuela_cursos;
use escuela_cursos;

create table cursos(
	id int primary key auto_increment,
    nombre varchar(100) not null
);

create database escuela_alumnos_cursos;
use escuela_alumnos_cursos;

create table alumnos_cursos(
	id int primary key auto_increment,
    alumno_id int not null,
    curso_id int not null,
    calificacion_final float
);

create database escuela_tareas;
use escuela_tareas;

create table tareas(
	id int primary key auto_increment,
    nombre varchar(100) not null,
    avalado_padre boolean not null default false,
    curso_id int not null
);