package com.nishant.feedslibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nishant.feedslibrary.R;
import com.nishant.feedslibrary.model.Posts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.CustomViewHolder> {

    private List<Posts> dataList;
    private Context context;
    /**
     * The human-readable descriptions for each sort mode.
     */
    private int[] mSortEntries = new int[]{R.string.date, R.string.views, R.string.likes, R.string.shares, R.string.name};
    /**
     * The index of the current of the current sort mode in mSortValues, or
     * the inverse of the index (in which case sort should be descending
     * instead of ascending).
     */
    private int mSortMode, mSortBy;

    public FeedsAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Posts> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addAll(List<Posts> newDataList) {
        int lastIndex = dataList.size() - 1;
        dataList.addAll(newDataList);
        notifyItemRangeInserted(lastIndex, newDataList.size());
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private final View mView;

        private ImageView coverImage;
        TextView title;
        TextView date;
        TextView views;
        TextView likes;
        TextView shares;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            coverImage = mView.findViewById(R.id.coverImage);
            title = mView.findViewById(R.id.title);
            title.setSelected(true);
            date = mView.findViewById(R.id.date);
            views = mView.findViewById(R.id.views);
            likes = mView.findViewById(R.id.likes);
            shares = mView.findViewById(R.id.shares);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (dataList != null && !dataList.isEmpty()) {
            holder.title.setText(dataList.get(position).getEvent_name());
            holder.date.setText(String.valueOf(getDate(dataList.get(position).getEvent_date())));
            holder.views.setText(String.valueOf(dataList.get(position).getViews()));
            holder.likes.setText(String.valueOf(dataList.get(position).getLikes()));
            holder.shares.setText(String.valueOf(dataList.get(position).getShares()));

            Glide.with(context).load(dataList.get(position).getThumbnail_image())
                    .dontAnimate()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.coverImage);
        }

    }

    @Override
    public int getItemCount() {
        return dataList != null && !dataList.isEmpty() ? dataList.size() : 0;
    }

    private String getDate(Integer timeStamp) {
        Date date = new Date((long) timeStamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    /**
     * Return the available sort modes for this adapter.
     *
     * @return An array containing the resource ids of the sort mode strings.
     */
    public int[] getSortEntries() {
        return mSortEntries;
    }

    /**
     * Set the sort by mode.
     *
     * @param i The index of the sort by mode in the sort entries array. If this
     *          is negative, the inverse of the index will be used and sort order will
     *          be reversed.
     */
    public void setSortBy(int i) {
        mSortBy = i;
    }

    /**
     * Return the current sort by mode set on this adapter.
     */
    public int getSortBy() {
        return mSortBy;
    }

    /**
     * Set the sorting mode.
     *
     * @param i The index of the sort mode in the sort entries array. If this
     *          is negative, the inverse of the index will be used and sort order will
     *          be reversed.
     */
    public void setSortMode(int i) {
        mSortMode = i;
    }

    /**
     * Return the current sort mode set on this adapter.
     */
    public int getSortMode() {
        return mSortMode;
    }

    public List<Posts> getAllItems() {
        List<Posts> result = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            result.add(dataList!=null?dataList.get(i):null);
        }
        return result;
    }
}
