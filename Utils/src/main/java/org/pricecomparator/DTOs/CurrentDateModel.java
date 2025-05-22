package org.pricecomparator.DTOs;

public class CurrentDateModel {
    private String currentDate;

    public CurrentDateModel(String currentDate) {
        this.currentDate = currentDate;
    }

    public CurrentDateModel() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
