package ie.dq.motorbike.service.impl;

import java.math.BigDecimal;

public class MarginCalculator {

    public static BigDecimal calculateMargin(BigDecimal costPrice, BigDecimal sellPrice){
        BigDecimal profit = sellPrice.subtract(costPrice);
        return profit.divide(costPrice).multiply(BigDecimal.valueOf(100));
    }

    public static BigDecimal calculateMargin(BigDecimal costPrice, BigDecimal sellPrice, BigDecimal vatRate, boolean priceVatInclusive, boolean marginVatInclusive) {
        if(marginVatInclusive){
            //margin calculation should include vat
            if(priceVatInclusive){
                //price and margin both include vat
                return calculateMargin(costPrice, sellPrice);
            }else {
                //add vat to price to include vat in margin
                BigDecimal vatAmount = sellPrice.divide(vatRate);
                BigDecimal includingVatSellPrice = sellPrice.add(vatAmount);
                return calculateMargin(costPrice, includingVatSellPrice);
            }
        }else {
            //margin calculation should not include vat
            if(priceVatInclusive){
                //remove vat from price for margin calculation
                BigDecimal vatAmount = sellPrice.divide(vatRate);
                BigDecimal excludingVatSellPrice = sellPrice.subtract(vatAmount);
                return calculateMargin(costPrice, excludingVatSellPrice);
            }else {
                //price and margin both exclude vat
                return calculateMargin(costPrice, sellPrice);
            }
        }
    }

}
