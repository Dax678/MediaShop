package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Exception.NotFoundException;
import org.example.mediashop.Data.DTO.DiscountDTO;
import org.example.mediashop.Data.Entity.Discount;
import org.example.mediashop.Repository.DiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.example.mediashop.Data.DTO.Mapper.DiscountMapper;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    private DiscountRepository discountRepository;

    public DiscountDTO getDiscountById(Long id) {
        Optional<Discount> discount = discountRepository.findById(id);

        if(discount.isEmpty()) {
            logger.warn("Discount with id: {} not found", id);
            throw new NotFoundException("Discount with id: {0} not found", id);
        }

        return DiscountMapper.toDiscountDTO(discount.get());
    }

    public Double getDiscountValueById(Long id) {
        return discountRepository.findById(id).map(Discount::getValue).orElse(0.0);
    }

    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        Discount discount = DiscountMapper.toDiscountEntity(discountDTO);

        return DiscountMapper.toDiscountDTO(discountRepository.save(discount));
    }

    public DiscountDTO getDiscountByCode(String discountCode) {
        Optional<Discount> discount = discountRepository.findByActiveCode(discountCode);

        if(discount.isEmpty()) {
            logger.warn("Discount with code: {} not found", discountCode);
            throw new NotFoundException("Discount with code: {0} not found", discountCode);
        }

        return DiscountMapper.toDiscountDTO(discount.get());
    }
}
