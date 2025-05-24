package org.pricecomparator.DTOs;

import java.util.List;
import java.util.Map;

public class ShoppingBasketMonitoringModel {

    Map<String, List<BestProductPriceModel>> shoppingModel;

    public ShoppingBasketMonitoringModel(Map<String, List<BestProductPriceModel>> shoppingModel) {
        this.shoppingModel = shoppingModel;
    }

    public ShoppingBasketMonitoringModel() {
    }

    public Map<String, List<BestProductPriceModel>> getShoppingModel() {
        return shoppingModel;
    }

    public void setShoppingModel(Map<String, List<BestProductPriceModel>> shoppingModel) {
        this.shoppingModel = shoppingModel;
    }
}
