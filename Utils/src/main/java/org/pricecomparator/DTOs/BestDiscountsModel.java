package org.pricecomparator.DTOs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestDiscountsModel {

    private Map<String, List<ProductDiscountModel>> bestStoreDiscounts;

    public BestDiscountsModel() {
        this.bestStoreDiscounts = new HashMap<>();
    }

    public Map<String, List<ProductDiscountModel>> getBestStoreDiscounts() {
        return bestStoreDiscounts;
    }

    public void setBestStoreDiscounts(Map<String, List<ProductDiscountModel>> bestStoreDiscounts) {
        this.bestStoreDiscounts = bestStoreDiscounts;
    }
}
