package org.pricecomparator.DTOs;

public class StoreCSVModel {
    private String fileName;
    private String storeName;
    private String date;

    public StoreCSVModel(String fileName, String storeName, String date) {
        this.fileName = fileName;
        this.storeName = storeName;
        this.date = date;
    }

    public StoreCSVModel() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
