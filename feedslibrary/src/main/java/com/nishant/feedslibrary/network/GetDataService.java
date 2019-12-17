package com.nishant.feedslibrary.network;

import com.nishant.feedslibrary.model.PostsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDataService {

    @GET
    Call<PostsList> getPosts(@Url String url);
}
