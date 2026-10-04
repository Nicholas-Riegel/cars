package com.riegelnick.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riegelnick.backend.services.HomeService;

@RestController
public class HomeController {

    final HomeService homeService;

    HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/api/home")
    public String getHomePage() {
        return homeService.homePage();
    }
}
