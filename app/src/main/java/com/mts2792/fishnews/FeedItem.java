package com.mts2792.fishnews;

/**
 * Created by Mitch Davis on 03/07/1.
 */
public class FeedItem {

    private String title;
    private String author;
//    private String thumbnail;
    private String image;
    private String getDescription;
    private String getUrl;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return getDescription;
    }

    public void setDescription(String getDate) {
        this.getDescription = getDate;
    }

    public String getUrl() {
        return getUrl;
    }

    public void setUrl(String getUrl) {
        this.getUrl = getUrl;
    }

//    public String getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
}
