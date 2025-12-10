package com.riegelnick.backend.services;

import org.springframework.stereotype.Service;

@Service
public class HomeService {
    
    public String homePage() {
        return "Hello!";
    }
}
