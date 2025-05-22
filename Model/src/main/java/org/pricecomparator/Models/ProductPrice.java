package org.pricecomparator.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class ProductPrice {
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) int id;
    private String productId;
    private int storeId;
    private float price;
    private String currency;
    private LocalDate date;

    public ProductPrice(String productId, int storeId, float price, String currency, LocalDate date) {
        this.productId = productId;
        this.storeId = storeId;
        this.price = price;
        this.currency = currency;
        this.date = date;
    }

    public ProductPrice(int id, String productId, int storeId, float price, String currency, LocalDate date) {
        this.id = id;
        this.productId = productId;
        this.storeId = storeId;
        this.price = price;
        this.currency = currency;
        this.date = date;
    }

    public ProductPrice() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
