
ALTER TABLE Student
    ADD CONSTRAINT chk_student_age CHECK (age >= 16),
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT unq_student_name UNIQUE (name),
    ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE Faculty
    ADD CONSTRAINT unq_faculty_name_color UNIQUE (name, color);