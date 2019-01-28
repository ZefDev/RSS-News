package com.example.news;

public class News {
    private String title;
    private String description;
    private String label;
    private String imageUrl;


    public News() {
    }

    public News(String title, String description, String label, String imageUrl) {
        this.title = title;
        this.description = description;
        this.label = label;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
