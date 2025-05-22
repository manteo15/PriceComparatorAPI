package org.pricecomparator.DTOs;

import com.opencsv.bean.CsvBindByName;

public class StorePricesCSVModel {
    @CsvBindByName(column = "product_id")
    private String product_id;
    @CsvBindByName(column = "product_name")
    private String product_name;
    @CsvBindByName(column = "product_category")
    private String product_category;
    @CsvBindByName(column = "brand")
    private String brand;
    @CsvBindByName(column = "package_quantity")
    private float package_quantity;
    @CsvBindByName(column = "package_unit")
    private String package_unit;
    @CsvBindByName(column = "price")
    private float price;
    @CsvBindByName(column = "currency")
    private String currency;

    public StorePricesCSVModel(String product_id, String product_name, String product_category, String brand,
                               float package_quantity, String package_unit, float price, String currency) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_category = product_category;
        this.brand = brand;
        this.package_quantity = package_quantity;
        this.package_unit = package_unit;
        this.price = price;
        this.currency = currency;
    }

    public StorePricesCSVModel() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getPackage_quantity() {
        return package_quantity;
    }

    public void setPackage_quantity(float package_quantity) {
        this.package_quantity = package_quantity;
    }

    public String getPackage_unit() {
        return package_unit;
    }

    public void setPackage_unit(String package_unit) {
        this.package_unit = package_unit;
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
}
