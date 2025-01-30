ALTER TABLE drivers_cars
ADD CONSTRAINT fk_driver_car_id
FOREIGN KEY (driver_id)
REFERENCES drivers (id)
ON UPDATE CASCADE;

ALTER TABLE drivers_cars
ADD CONSTRAINT fk_car_driver_id
FOREIGN KEY (car_id)
REFERENCES cars (id)
ON UPDATE CASCADE;