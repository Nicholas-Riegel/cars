package com.riegelnick.backend.repositories;

import com.riegelnick.backend.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    // That's it! JpaRepository provides all basic CRUD methods automatically

    // save(Car car) - Create or update
    // findById(Long id) - Find by ID
    // findAll() - Get all cars
    // deleteById(Long id) - Delete by ID
    // count() - Count total cars
    // And many more!

    // The magic:
    // JpaRepository<Car, Long> means: "Repository for Car entity with Long ID type"
    // Spring Data JPA implements all the methods automatically at runtime
    // You just declare the interface, no implementation needed!

    // You can add custom queries later (optional):
    // List<Car> findByMake(String make);
    // List<Car> findByYear(Integer year);
    
    // save() handles both create and update!
    // Create new car
    // Car newCar = new Car("Toyota", "Camry", 2020, "Nice car");
    // carRepository.save(newCar); // INSERT - id is null, so creates new record

    // // Update existing car
    // Car existingCar = carRepository.findById(1L).get();
    // existingCar.setDescription("Updated description");
    // carRepository.save(existingCar); // UPDATE - id exists, so updates existing record
}