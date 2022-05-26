CREATE TABLE IF NOT EXISTS course
(uuid varchar(36) NOT NULL PRIMARY KEY, 
course_name varchar(255) NOT NULL, 
course_description varchar(1024) NOT NULL ); 

CREATE TABLE IF NOT EXISTS group_1
( uuid varchar(36) PRIMARY KEY, 
group_name varchar(100) NOT NULL ); 

CREATE TABLE IF NOT EXISTS student
(uuid varchar(36) NOT NULL PRIMARY KEY, 
first_name varchar(20) NOT NULL, 
last_name varchar(20) NOT NULL, 
group_uuid varchar(36) NULL 
	REFERENCES  group_1(uuid) 
	ON DELETE SET NULL ); 

CREATE TABLE IF NOT EXISTS students_on_course
(student_uuid varchar(36) NOT NULL 
	REFERENCES student( uuid ), 
course_uuid varchar(36) NOT NULL 
	REFERENCES course ( uuid ), 
PRIMARY KEY (student_uuid, course_uuid)); 
