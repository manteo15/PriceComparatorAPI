package org.pricecomparator.DTOs;

import com.opencsv.bean.CsvBindByName;

public class StoreDiscountsCSVModel {
    @CsvBindByName(column = "product_id")
    private String product_id;
    @CsvBindByName(column = "from_date")
    private String from_date;
    @CsvBindByName(column = "to_date")
    private String to_date;
    @CsvBindByName(column = "percentage_of_discount")
    private int percentage_of_discount;

    public StoreDiscountsCSVModel(String product_id, String from_date, String to_date, int percentage_of_discount) {
        this.product_id = product_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.percentage_of_discount = percentage_of_discount;
    }

    public StoreDiscountsCSVModel() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public int getPercentage_of_discount() {
        return percentage_of_discount;
    }

    public void setPercentage_of_discount(int percentage_of_discount) {
        this.percentage_of_discount = percentage_of_discount;
    }
}
