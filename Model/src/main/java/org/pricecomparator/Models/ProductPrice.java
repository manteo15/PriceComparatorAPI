package org.pricecomparator.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class ProductPrice {
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) int id;
    private String productId;
    private int storeId;
    private float price;
    private String currency;
    private Date date;

    public ProductPrice(String productId, int storeId, float price, String currency, Date date) {
        this.productId = productId;
        this.storeId = storeId;
        this.price = price;
        this.currency = currency;
        this.date = date;
    }

    public ProductPrice(int id, Date date, String currency, float price, int storeId, String productId) {
        this.id = id;
        this.date = date;
        this.currency = currency;
        this.price = price;
        this.storeId = storeId;
        this.productId = productId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
