
SELECT s.name, s.age, f.name AS faculty_name
FROM Student s
LEFT JOIN Faculty f ON s.faculty_id = f.faculty_id
WHERE f.name = 'Хогвартс';

SELECT s.name, s.age
FROM Student s
INNER JOIN StudentAvatar sa ON s.student_id = sa.student_id;
