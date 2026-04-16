package mx.com.test.nelo.alexis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Root", description = "API Root Information")
public class RootController {
    
    @GetMapping("/")
    @Operation(summary = "API Root", description = "Get API information and endpoints")
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Nelo Restaurant Booking API");
        response.put("version", "1.0.0");
        response.put("description", "Social Restaurant Booking API for finding restaurants with dietary restrictions and making reservations");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("search", "POST /api/v1/restaurants/search - Search available restaurants");
        endpoints.put("reserve", "POST /api/v1/reservations - Create reservation");
        endpoints.put("docs", "/api/v1/swagger-ui.html - API Documentation");
        endpoints.put("h2-console", "/api/v1/h2-console - Database Console");
        
        response.put("endpoints", endpoints);
        return response;
    }
}
