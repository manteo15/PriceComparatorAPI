package org.pricecomparator.DTOs;

import java.util.List;
import java.util.Map;

public class DynamicPriceHistoryModel {

    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private float packageQuantity;
    private String packageUnit;
    private Map<Integer,List<PriceTrendModel>> priceTrends;

    public DynamicPriceHistoryModel( String productId, String productName, String productCategory, String productBrand, float packageQuantity,
                                    String packageUnit, Map<Integer,List<PriceTrendModel>> priceTrends) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.priceTrends = priceTrends;
    }

    public DynamicPriceHistoryModel(String productId, String productName, String productCategory, String productBrand, float packageQuantity, String packageUnit) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
    }

    public DynamicPriceHistoryModel() {
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

    public Map<Integer,List<PriceTrendModel>> getPriceTrends() {
        return priceTrends;
    }

    public void setPriceTrends(Map<Integer,List<PriceTrendModel>> priceTrends) {
        this.priceTrends = priceTrends;
    }
}
