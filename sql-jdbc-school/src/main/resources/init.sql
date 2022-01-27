DROP TABLE IF EXISTS cources; 
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
CREATE TABLE IF NOT EXISTS cources(id serial primary key, name varchar(255), description varchar(1024));
CREATE TABLE IF NOT EXISTS groups(id serial primary key, name varchar(100));
CREATE TABLE IF NOT EXISTS students(id serial primary key, first_name varchar(20), second_name varchar(20),group_id integer REFERENCES groups(id))