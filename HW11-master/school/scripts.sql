
SELECT id, name FROM student WHERE age BETWEEN 10 AND 20;

SELECT name FROM student;

SELECT id, name, age FROM student WHERE name ILIKE '%О%';

SELECT id, name, age, faculty_id FROM student WHERE age < id;

SELECT name, age FROM student ORDER BY age;
