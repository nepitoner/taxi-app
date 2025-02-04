CREATE TABLE IF NOT EXISTS drivers_cars
(
    driver_id UUID NOT NULL,
    car_id UUID NOT NULL,
    PRIMARY KEY (driver_id, car_id)
);