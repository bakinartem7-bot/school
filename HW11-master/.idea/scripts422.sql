
CREATE TABLE Person (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    has_driver_license BOOLEAN NOT NULL
);

CREATE TABLE Car (
    car_id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL
);

CREATE TABLE PersonCar (
    person_id INT NOT NULL,
    car_id INT NOT NULL,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES Person(person_id),
    FOREIGN KEY (car_id) REFERENCES Car(car_id)
);
