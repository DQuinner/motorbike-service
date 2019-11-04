package ie.dq.motorbike.service.impl;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MarginCalculatorTest {

    BigDecimal costPrice;
    BigDecimal sellPrice;
    BigDecimal vatRate;

    @Before
    public void setUp() throws Exception {
        costPrice = BigDecimal.valueOf(50);
        sellPrice = BigDecimal.valueOf(100);
        vatRate = BigDecimal.TEN;
    }

    @Test
    public void calculateMargin() {
        BigDecimal marginPercentage = MarginCalculator.calculateMargin(costPrice, sellPrice);
        assertEquals(BigDecimal.valueOf(100), marginPercentage);
    }

    @Test
    public void calculateMarginPriceAndMarginNotVatInclusive() {
        boolean priceVatInclusive = false;
        boolean marginVatInclusive = false;
        BigDecimal marginPercentage = MarginCalculator.calculateMargin(costPrice, sellPrice, vatRate, priceVatInclusive, marginVatInclusive);
        assertEquals(BigDecimal.valueOf(100), marginPercentage);
    }

    @Test
    public void calculateMarginPriceAndMarginVatInclusive() {
        boolean priceVatInclusive = true;
        boolean marginVatInclusive = true;
        BigDecimal marginPercentage = MarginCalculator.calculateMargin(costPrice, sellPrice, vatRate, priceVatInclusive, marginVatInclusive);
        assertEquals(BigDecimal.valueOf(100), marginPercentage);
    }

    @Test
    public void calculateMarginPriceVatInclusiveAndMarginNotVatInclusive() {
        boolean priceVatInclusive = true;
        boolean marginVatInclusive = false;
        BigDecimal marginPercentage = MarginCalculator.calculateMargin(costPrice, sellPrice, vatRate, priceVatInclusive, marginVatInclusive);
        assertEquals(BigDecimal.valueOf(80.0d), marginPercentage);
    }

    @Test
    public void calculateMarginPriceNotVatInclusiveAndMarginVatInclusive() {
        boolean priceVatInclusive = false;
        boolean marginVatInclusive = true;
        BigDecimal marginPercentage = MarginCalculator.calculateMargin(costPrice, sellPrice, vatRate, priceVatInclusive, marginVatInclusive);
        assertEquals(BigDecimal.valueOf(120.0d), marginPercentage);
    }

}