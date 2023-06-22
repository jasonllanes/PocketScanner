package com.example.pocketscanner.data;

public class NewsData {
    public String name,date_released,img_link;

    public NewsData(){}

    public NewsData(String name, String date_released, String img_link) {
        this.name = name;
        this.date_released = date_released;
        this.img_link = img_link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_released() {
        return date_released;
    }

    public void setDate_released(String date_released) {
        this.date_released = date_released;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
