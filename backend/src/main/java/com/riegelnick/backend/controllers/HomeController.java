package com.riegelnick.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riegelnick.backend.services.HomeService;

@RestController
public class HomeController {

    @Autowired
    HomeService homeService;

    @GetMapping("/api/home")
    public String getHomePage() {
        return homeService.homePage();
    }
}
