package com.example.SafeBlurrinder.controller;

import lombok.Getter;
import lombok.Setter;

public class Product {
    private int url_id;
    private String title;
    private String name;
    private String day;

    public int getUrl_id() {
        return url_id;
    }

    public void setUrl_id(int url_id) {
        this.url_id = url_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Product(int url_id, String title, String name, String day) {
        this.url_id = url_id;
        this.title = title;
        this.name = name;
        this.day = day;
    }
}
