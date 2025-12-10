package com.riegelnick.backend.services;

import com.riegelnick.backend.entities.Car;
import com.riegelnick.backend.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CarService {
    
    @Autowired
    private CarRepository carRepository;
    
    // Create a new car
    public Car createCar(Car car) {
        return carRepository.save(car);
    }
    
    // Get all cars
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
    // Get a car by ID
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    // Update a car
    public Car updateCar(Long id, Car carDetails) {
        Car car = carRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Car not found"));
        
        if (carDetails.getMake() != null) {
            car.setMake(carDetails.getMake());
        }
        if (carDetails.getModel() != null) {
            car.setModel(carDetails.getModel());
        }
        if (carDetails.getYear() != null) {
            car.setYear(carDetails.getYear());
        }
        if (carDetails.getDescription() != null) {
            car.setDescription(carDetails.getDescription());
        }
        
        return carRepository.save(car);
    }
    
    // Delete a car
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
    }   
}