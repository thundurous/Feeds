package com.nishant.feedslibrary.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nishant.feedslibrary.model.Posts;

import java.util.List;

public class SavePosts extends AsyncTask<Void, Long, Long> {

    private List<Posts> dataList;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public SavePosts(List<Posts> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    protected Long doInBackground(Void... voids) {

        Long result=0L;
        for(Posts data: dataList){
            Posts posts = new Posts(data.getId(), data.getThumbnail_image(), data.getEvent_name(),
                    data.getEvent_date(), data.getViews(), data.getLikes(), data.getShares());

            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
            result = appDatabase.postDao().insertPost(posts);
        }

        return result;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        if (result!=null) {
            if (result>0)
                Toast.makeText(context, "Data Saving Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "Data Saving Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
