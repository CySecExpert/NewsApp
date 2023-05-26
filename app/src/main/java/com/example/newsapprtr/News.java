package com.example.newsapprtr;
import java.io.*;
import java.util.*;
public class News {
    private List<Sourcenews> sourcenews;
    private String status;
    public News(String status, List<Sourcenews> sourcenews){
        this.status = status;
        this.sourcenews = sourcenews;
    }
    public List<Sourcenews> getSourcenews(){
        return sourcenews;
    }
}
