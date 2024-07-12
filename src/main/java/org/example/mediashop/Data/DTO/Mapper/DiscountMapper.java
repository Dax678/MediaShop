package org.example.mediashop.Data.DTO.Mapper;

import org.example.mediashop.Data.DTO.DiscountDTO;
import org.example.mediashop.Data.Entity.Discount;

public class DiscountMapper {

    public static DiscountDTO toDiscountDTO(Discount discount) {
        return new DiscountDTO(
                discount.getId(),
                discount.getValue(),
                discount.getCode(),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getMaxUsage(),
                discount.getMinPurchaseAmount()
        );
    }

    public static Discount toDiscountEntity(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setId(discountDTO.getId());
        discount.setValue(discountDTO.getValue());
        discount.setCode(discountDTO.getCode());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setMaxUsage(discountDTO.getMaxUsage());
        discount.setMinPurchaseAmount(discountDTO.getMinPurchaseAmount());
        return discount;
    }
}
