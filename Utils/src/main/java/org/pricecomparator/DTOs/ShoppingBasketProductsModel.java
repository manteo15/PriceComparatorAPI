package org.pricecomparator.DTOs;

import java.util.List;

public class ShoppingBasketProductsModel {

    private List<String> productList;
    private String currentDate;

    public ShoppingBasketProductsModel(List<String> productList, String currentDate) {
        this.productList = productList;
        this.currentDate = currentDate;
    }

    public ShoppingBasketProductsModel() {
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
