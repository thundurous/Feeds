package com.nishant.feedslibrary.database;

import com.nishant.feedslibrary.model.Posts;

import java.util.List;

public interface OnDataFetchedFromDBListener {

    void setDataListFromDB(List<Posts> dataListFromDB, final boolean isFirstPage, final Integer currentPage);
}
