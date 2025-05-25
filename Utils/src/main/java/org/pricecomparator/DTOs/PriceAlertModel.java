package org.pricecomparator.DTOs;

public class PriceAlertModel {
    private String productId;
    private String currentDate;
    private float targetPrice;

    public PriceAlertModel(String productId, String currentDate, float targetPrice) {
        this.productId = productId;
        this.currentDate = currentDate;
        this.targetPrice = targetPrice;
    }

    public PriceAlertModel() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public float getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(float targetPrice) {
        this.targetPrice = targetPrice;
    }
}
