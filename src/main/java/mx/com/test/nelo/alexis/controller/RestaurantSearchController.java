package mx.com.test.nelo.alexis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mx.com.test.nelo.alexis.dto.RestaurantSearchBeanParam;
import mx.com.test.nelo.alexis.dto.RestaurantSearchResponse;
import mx.com.test.nelo.alexis.service.RestaurantSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@Tag(name = "Restaurant Search", description = "APIs for searching available restaurants")
public class RestaurantSearchController {
    
    @Autowired
    private RestaurantSearchService restaurantSearchService;
    
    @GetMapping("/search")
    @Operation(summary = "Search available restaurants", 
               description = "Find restaurants with available tables that match dietary restrictions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found available restaurants"),
        @ApiResponse(responseCode = "400", description = "Invalid search request")
    })
    public ResponseEntity<List<RestaurantSearchResponse>> searchRestaurants(
            @Valid @ModelAttribute RestaurantSearchBeanParam searchParams) {
        
        List<RestaurantSearchResponse> results = restaurantSearchService.findAvailableRestaurants(searchParams);
        return ResponseEntity.ok(results);
    }
}
