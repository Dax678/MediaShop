package org.example.mediashop.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.example.mediashop.Data.DTO.DiscountDTO;
import org.example.mediashop.Service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/discounts")
public class DiscountController {
    private static final Logger logger = LoggerFactory.getLogger(DiscountController.class);

    private DiscountService discountService;

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Map<String, DiscountDTO>> getDiscountById(@PathVariable("id") @Positive Long id) {
        DiscountDTO discountDTO = discountService.getDiscountById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("discount", discountDTO));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Map<String, DiscountDTO>> createDiscount(@Valid @RequestBody DiscountDTO discountDTO) {
        DiscountDTO discount = discountService.createDiscount(discountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("discount", discount));
    }
}
