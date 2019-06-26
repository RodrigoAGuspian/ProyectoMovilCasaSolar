package com.casasolarctpi.appsolar.models;

public class SummaryData {
    private String title, data;

    public SummaryData() {
    }

    public SummaryData(String title, String data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
