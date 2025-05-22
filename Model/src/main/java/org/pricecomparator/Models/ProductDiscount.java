package org.pricecomparator.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class ProductDiscount {
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) int id;
    private String productId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentDiscount;
    private int storeId;

    public ProductDiscount(String productId, LocalDate fromDate, LocalDate toDate, int percentDiscount, int storeId) {
        this.productId = productId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percentDiscount = percentDiscount;
        this.storeId = storeId;
    }

    public ProductDiscount(int id, String productId, LocalDate fromDate, LocalDate toDate, int percentDiscount, int storeId) {
        this.id = id;
        this.productId = productId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percentDiscount = percentDiscount;
        this.storeId = storeId;
    }

    public ProductDiscount() {
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

    public int getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
