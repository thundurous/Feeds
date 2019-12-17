package com.nishant.feedslibrary.database;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.nishant.feedslibrary.model.Posts;

import java.util.List;

public class FetchPosts extends AsyncTask<Void, List<Posts>, List<Posts>> {

    private ProgressDialog progressDialog;
    private final boolean isFirstPage;
    private final Integer currentPage;
    @SuppressLint("StaticFieldLeak")
    private AppCompatActivity appCompatActivity;
    private OnDataFetchedFromDBListener onDataFetchedFromDBListener;

    public FetchPosts(AppCompatActivity appCompatActivity, final boolean isFirstPage, final Integer currentPage, OnDataFetchedFromDBListener onDataFetchedFromDBListener) {
        this.appCompatActivity = appCompatActivity;
        this.isFirstPage = isFirstPage;
        this.currentPage = currentPage;
        this.onDataFetchedFromDBListener = onDataFetchedFromDBListener;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(appCompatActivity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data....");
        if(progressDialog!=null && !progressDialog.isShowing() && appCompatActivity.getWindow().getDecorView().isShown())
            progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected List<Posts> doInBackground(Void... voids) {
        return AppDatabase.getAppDatabase(appCompatActivity).postDao().getAllPosts();
    }

    @Override
    protected void onPostExecute(List<Posts> posts) {
        super.onPostExecute(posts);
        if(progressDialog!=null && progressDialog.isShowing() && appCompatActivity.getWindow().getDecorView().isShown())
            progressDialog.dismiss();

        onDataFetchedFromDBListener.setDataListFromDB(posts, isFirstPage, currentPage);
    }
}
