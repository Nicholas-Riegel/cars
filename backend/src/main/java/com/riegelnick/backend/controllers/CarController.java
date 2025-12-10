package com.riegelnick.backend.controllers;

import com.riegelnick.backend.entities.Car;
import com.riegelnick.backend.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    // Create a new car
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.createCar(car);
    }
    
    // Get all cars
    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    // Get a car by ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id) // Returns Optional<Car>
            .map(ResponseEntity::ok) // If Car exists, wrap it in ResponseEntity with 200 OK. =.map(car -> ResponseEntity.ok(car))
            .orElse(ResponseEntity.notFound().build()); // If empty, return 404 Not Found
    }
    
    // Update a car
    @PatchMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        try {
            Car updatedCar = carService.updateCar(id, carDetails);
            return ResponseEntity.ok(updatedCar); // Returns ResponseEntity<Car>
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Java infers: "This must be ResponseEntity<Car> because that's what the method returns"
        }
    }

    // Delete a car
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}