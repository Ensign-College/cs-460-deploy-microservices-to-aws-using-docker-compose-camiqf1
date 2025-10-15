package com.example.explorecalijpa.recommendation;

import java.util.List;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommendations")
@Validated
public class RecommendationController {

  private final RecommendationService service;

  public RecommendationController(RecommendationService service) {
    this.service = service;
  }

  @GetMapping("/top/{limit}")
  public List<TourRecommendation> top(@PathVariable @Min(1) @Max(100) int limit) {
    return service.recommendTopN(limit);
  }

  @GetMapping("/customer/{customerId}")
  public List<TourRecommendation> forCustomer(
      @PathVariable @Min(1) int customerId,
      @RequestParam(defaultValue = "5") @Min(1) @Max(100) int limit) {
    return service.recommendForCustomer(customerId, limit);
  }

  @DeleteMapping("/cache")
  @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
  public void clearCache() {
    service.evictAll();
  }
}
