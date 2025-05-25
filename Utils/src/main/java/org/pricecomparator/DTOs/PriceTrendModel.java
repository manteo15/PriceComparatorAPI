package org.pricecomparator.DTOs;

import java.time.LocalDate;

public class PriceTrendModel {

    private float price;
    private String currency;
    private String storeName;
    private int discountPercentage;
    private LocalDate fromDate;
    private LocalDate toDate;

    public PriceTrendModel(float price, String currency, String storeName, int discountPercentage, LocalDate fromDate, LocalDate toDate) {
        this.price = price;
        this.currency = currency;
        this.storeName = storeName;
        this.discountPercentage = discountPercentage;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public PriceTrendModel() {
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
