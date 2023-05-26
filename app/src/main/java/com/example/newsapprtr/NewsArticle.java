package com.example.newsapprtr;

public class NewsArticle {
    private String authors, dateTime, headline, articleDesc, imageURL, articleURL;

    public String getAuthor() {
        return authors;
    }
    public String getHeadline() {
        return headline;
    }

    public String getURL() {
        return articleURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }

    public void setURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setAuthor(String authors) {
        this.authors = authors;
    }
}
