CREATE TABLE IF NOT EXISTS drivers_cars
(
    driver_id UUID NOT NULL,
    car_id UUID NOT NULL,
    PRIMARY KEY (driver_id, car_id),
    CONSTRAINT fk_driver_car_id FOREIGN KEY (driver_id) REFERENCES drivers (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_car_driver_id FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE ON UPDATE CASCADE
);