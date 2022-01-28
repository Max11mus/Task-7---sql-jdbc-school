CREATE TABLE IF NOT EXISTS course(id uuid NOT NULL UNIQUE PRIMARY KEY, course_name varchar(255) NOT NULL, course_description varchar(1024) NOT NULL);
CREATE TABLE IF NOT EXISTS squad(id uuid NOT NULL UNIQUE PRIMARY KEY, squad_name varchar(100) NOT NULL);
CREATE TABLE IF NOT EXISTS student(id uuid NOT NULL UNIQUE PRIMARY KEY, student_first_name varchar(20) NOT NULL, student_last_name varchar(20) NOT NULL, squad_id uuid REFERENCES squad(id));
CREATE TABLE IF NOT EXISTS students_on_course(student_id uuid NOT NULL REFERENCES student(id), course_id uuid NOT NULL REFERENCES course(id), PRIMARY KEY(student_id, course_id)); 