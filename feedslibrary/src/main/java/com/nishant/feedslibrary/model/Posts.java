package com.nishant.feedslibrary.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "feeds")
public class Posts {

    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;
    @SerializedName("thumbnail_image")
    @ColumnInfo(name = "thumbnail_image")
    private String thumbnail_image;
    @SerializedName("event_name")
    @ColumnInfo(name = "event_name")
    private String event_name;
    @SerializedName("event_date")
    @PrimaryKey(autoGenerate = true)
    private Integer event_date;
    @SerializedName("views")
    @ColumnInfo(name = "views")
    private Integer views;
    @SerializedName("likes")
    @ColumnInfo(name = "likes")
    private Integer likes;
    @SerializedName("shares")
    @ColumnInfo(name = "shares")
    private Integer shares;

    public Posts(String id, String thumbnail_image, String event_name, Integer event_date, Integer views, Integer likes, Integer shares) {
        this.id = id;
        this.thumbnail_image = thumbnail_image;
        this.event_name = event_name;
        this.event_date = event_date;
        this.views = views;
        this.likes = likes;
        this.shares = shares;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public Integer getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Integer event_date) {
        this.event_date = event_date;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }
}
