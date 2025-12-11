package com.riegelnick.backend.controllers;

import com.riegelnick.backend.entities.Car;
import com.riegelnick.backend.services.CarService;
import com.riegelnick.backend.services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private FileStorageService fileStorageService;

    // Create a new car without image
    @PostMapping("/upload-without-image")
    public ResponseEntity<Car> createCarWithoutImage(@RequestBody Car car) {
        Car savedCar = carService.createCar(car);
        return ResponseEntity.ok(savedCar);
    }
    
    // Create a new car with image
    @PostMapping("/upload-with-image")
    public ResponseEntity<Car> createCarWithImage(
        @RequestParam("make") String make,
        @RequestParam("model") String model,
        @RequestParam(value = "year", required = false) Integer year,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            
            String filename = null;
            if (file != null && !file.isEmpty()) {
                filename = fileStorageService.storeFile(file);
            }

            Car car = new Car(make, model, year, description, filename);
            Car savedCar = carService.createCar(car);
            
            return ResponseEntity.ok(savedCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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