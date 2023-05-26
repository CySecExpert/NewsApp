package com.example.newsapprtr;

public class Sourcenews {
    private String category, name, id;
    //Initializing the constructor...
    public Sourcenews(String id, String name, String category){
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
