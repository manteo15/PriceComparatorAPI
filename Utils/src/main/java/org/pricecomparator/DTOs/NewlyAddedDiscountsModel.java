package org.pricecomparator.DTOs;

public class NewlyAddedDiscountsModel {
    private String storeName;
    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private float packageQuantity;
    private String packageUnit;
    private String valuePerUnit;
    private int storeId;
    private float price;
    private String currency;
    private int percentageOfDiscount;

    public NewlyAddedDiscountsModel(String storeName, String productId, String productName, String productCategory,
                                    String productBrand, float packageQuantity, String packageUnit, String valuePerUnit, float price, String currency,
                                    int percentageOfDiscount, int storeId) {
        this.storeName = storeName;
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.valuePerUnit = valuePerUnit;
        this.price = price;
        this.currency = currency;
        this.percentageOfDiscount = percentageOfDiscount;
        this.storeId = storeId;
    }

    public NewlyAddedDiscountsModel() {
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public float getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(float packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
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

    public int getPercentageOfDiscount() {
        return percentageOfDiscount;
    }

    public void setPercentageOfDiscount(int percentageOfDiscount) {
        this.percentageOfDiscount = percentageOfDiscount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getValuePerUnit() {
        return valuePerUnit;
    }

    public void setValuePerUnit(String valuePerUnit) {
        this.valuePerUnit = valuePerUnit;
    }
}
