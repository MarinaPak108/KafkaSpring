package com.coffee.kafkaspringproject.controller;

import com.coffee.kafkaspringproject.service.RoastingService;
import com.coffee.kafkaspringproject.service.StockLossService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "Coffee Company", description = "Coffee Api")
@RestController
public class Controller {

    @Autowired
    private StockLossService stockLossService;

    //redirects to swagger
    @GetMapping("/")
    ModelAndView home(){
       return new ModelAndView("redirect:/swagger-ui/index.html");
    }

    @Operation(
            summary = "shows all coffee remainders aka stock",
            description = "shows coffee remainder after roasting filtered by origin country and/or coffee sort. if nothing is entered, then shows all remainders")
    @GetMapping("/stock")
    public ResponseEntity<?> getStock(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer coffeeSort) {
        try{
            int sum = stockLossService.getFilteredStock(country, coffeeSort);
            return ResponseEntity.ok(sum);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @Operation(
            summary = "shows loss percentage after roasting",
            description = "shows mean loss percentage after roasting filtered by team and/or origin country. if record with combination team with country cant be found, shows error message")
    @GetMapping("/loss-percentage")
    public ResponseEntity<?> getLossPercentage(
            @RequestParam(required = false) String teamId,
            @RequestParam(required = false) String country) {
        try{
            double lossPercentage = stockLossService.getLossPercentage(teamId, country);
            return ResponseEntity.ok(lossPercentage);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
