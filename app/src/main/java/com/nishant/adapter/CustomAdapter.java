package com.nishant.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.nishant.feedslibrary.adapter.FeedsAdapter;

public class CustomAdapter extends FeedsAdapter {

    public CustomAdapter(Context context) {
        super(context);
    }

    @Override
    public FeedsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
