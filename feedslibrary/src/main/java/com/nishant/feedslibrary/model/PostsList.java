package com.nishant.feedslibrary.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostsList {

    @SerializedName("posts")
    private List<Posts> posts = null;
    @SerializedName("page")
    private Integer page;

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
