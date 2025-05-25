package org.pricecomparator.DTOs;

public class PriceHistoryFilterModel {

    private Integer storeId;
    private String brand;
    private String category;
    private String currentDate;

    public PriceHistoryFilterModel(Integer storeId, String brand, String category, String currentDate) {
        this.storeId = storeId;
        this.brand = brand;
        this.category = category;
        this.currentDate = currentDate;
    }

    public PriceHistoryFilterModel() {
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
