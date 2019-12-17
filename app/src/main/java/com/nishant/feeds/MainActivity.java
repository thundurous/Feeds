package com.nishant.feeds;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.nishant.adapter.CustomAdapter;
import com.nishant.constants.Constants;
import com.nishant.feedslibrary.database.OnDataFetchedFromDBListener;
import com.nishant.feedslibrary.database.FetchPosts;
import com.nishant.feedslibrary.database.SavePosts;
import com.nishant.feedslibrary.model.Posts;
import com.nishant.feedslibrary.model.PostsList;
import com.nishant.feedslibrary.network.GetDataService;
import com.nishant.feedslibrary.network.RetrofitClientInstance;
import com.nishant.feedslibrary.utilities.CommonUtilities;
import com.nishant.feedslibrary.utilities.CustomComparator;

import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnDataFetchedFromDBListener, DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDailog;
    private boolean mIsLoading, mIsLastPage;
    private Integer page;
    private Integer totalPages = 3;
    final int MENU_SORT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDailog = new ProgressDialog(MainActivity.this);
        progressDailog.setMessage("Loading....");
        progressDailog.setCancelable(false);
        progressDailog.show();

        loadMoreItems(true, 0);
    }

    private void loadMoreItems(final boolean isFirstPage, Integer mCurrentPage) {
        // change loading state
        mIsLoading = true;
        final Integer finalMCurrentPage = mCurrentPage;
        mCurrentPage = mCurrentPage + 1;
        final Integer currentPage = mCurrentPage;

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(Constants.BASE_URL).create(GetDataService.class);
        Call<PostsList> call;

        if (mCurrentPage == 1) call = service.getPosts("v2/59b3f0b0100000e30b236b7e");
        else if (mCurrentPage == 2) call = service.getPosts("v2/59ac28a9100000ce0bf9c236");
        else call = service.getPosts("v2/59ac293b100000d60bf9c239");

        if (CommonUtilities.isConnectingToInternet(this)) {
            call.enqueue(new Callback<PostsList>() {
                @Override
                public void onResponse(Call<PostsList> call, Response<PostsList> response) {
                    progressDailog.dismiss();
                    if (response.body() != null)
                        generateDataList(response.body().getPosts(), isFirstPage);

                    mIsLoading = false;
                    mIsLastPage = currentPage == totalPages;
                    page = response.body().getPage();
                }

                @Override
                public void onFailure(Call<PostsList> call, Throwable t) {
                    progressDailog.dismiss();
                    new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setMessage("Something went wrong...Please try again later!")
                            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }
            });
        } else {
            progressDailog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("No Internet Connection!")
                    .setMessage("Please choose an option to proceed.")
                    .setPositiveButton("Go Offline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new FetchPosts(MainActivity.this, isFirstPage, finalMCurrentPage,
                                    MainActivity.this).execute();
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadMoreItems(isFirstPage, finalMCurrentPage);
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    Button neutralButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5f);
                    negativeButton.setLayoutParams(params);
                    positiveButton.setLayoutParams(params);
                    neutralButton.setLayoutParams(params);

                    negativeButton.invalidate();
                    positiveButton.invalidate();
                    neutralButton.invalidate();
                }
            });
            if (alertDialog != null && !alertDialog.isShowing())
                alertDialog.show();
        }
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Posts> dataList, boolean isFirstPage) {
        recyclerView = findViewById(R.id.customRecyclerView);
        if (adapter == null) adapter = new CustomAdapter(this);
        Collections.sort(dataList, new CustomComparator(adapter.getSortMode(), adapter.getSortBy()));
        if (isFirstPage) adapter.setList(dataList);
        else adapter.addAll(dataList);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(adapter);
        if(CommonUtilities.isConnectingToInternet(this))
            new SavePosts(dataList, this).execute();
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final int pageSize = 7;
            // number of visible items
            int visibleItemCount = layoutManager.getChildCount();
            // number of items in layout
            int totalItemCount = layoutManager.getItemCount();
            // the position of first visible item
            int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

            boolean isNotLoadingAndNotLastPage = !mIsLoading && !mIsLastPage;
            // flag if number of visible items is at the last
            boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
            // validate non negative values
            boolean isValidFirstItem = firstVisibleItemPosition >= 0;
            // validate total items are more than possible visible items
            boolean totalIsMoreThanVisible = totalItemCount >= pageSize;
            // flag to know whether to load more
            boolean shouldLoadMore = isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage;

            if (shouldLoadMore) {
                if (page == 1) loadMoreItems(false, 1);
                else if (page == 2) loadMoreItems(false, 2);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SORT, 0, R.string.sort_by).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(MENU_SORT).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SORT:
                CustomAdapter customAdapter = adapter;
                int mode = customAdapter.getSortMode();
                int check;
                if (mode < 0) {
                    check = R.id.descending;
                    mode = ~mode;
                } else {
                    check = R.id.ascending;
                }

                int[] itemIds = customAdapter.getSortEntries();
                String[] items = new String[itemIds.length];
                Resources res = getResources();
                for (int i = itemIds.length; --i != -1; ) {
                    items[i] = res.getString(itemIds[i]);
                }

                RadioGroup header = (RadioGroup) getLayoutInflater().inflate(R.layout.sort_dialog, null);
                header.check(check);

                int sortBy = customAdapter.getSortBy();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.sort_by);
                builder.setSingleChoiceItems(items, sortBy, this); // add 1 for header
//                builder.setNeutralButton(R.string.done, this);

                AlertDialog dialog = builder.create();
                dialog.getListView().addHeaderView(header);
                dialog.setOnDismissListener(this);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ListView list = ((AlertDialog) dialog).getListView();
        // subtract 1 for header
        int mode = list.getCheckedItemPosition() - 1;

        RadioGroup group = (RadioGroup) list.findViewById(R.id.sort_direction);
        if (group.getCheckedRadioButtonId() == R.id.descending)
            mode = ~mode;

        setSortMode(mode, which);
        dialog.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    /**
     * Set the sort mode for the current adapter. Saves this sort mode to preferences and updates the list
     * associated with the adapter to display the new sort mode.
     *
     * @param mode The sort mode.
     */
    public void setSortMode(int mode, int which) {
        CustomAdapter customAdapter = adapter;
        if (mode == customAdapter.getSortMode() && which == customAdapter.getSortBy())
            return;

        customAdapter.setSortMode(mode);
        customAdapter.setSortBy(which);
        List<Posts> dataList = customAdapter.getAllItems();
        Collections.sort(dataList, new CustomComparator(mode, which));
        customAdapter.setList(dataList);
    }

    @Override
    public void setDataListFromDB(List<Posts> dataListFromDB, final boolean isFirstPage, final Integer currentPage) {
        if(dataListFromDB!=null && !dataListFromDB.isEmpty()) {
            generateDataList(dataListFromDB, isFirstPage);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Data Could Not Be Fetched!")
                    .setMessage("Please choose an option to proceed.")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    })
                    .setNegativeButton("Go Online", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadMoreItems(isFirstPage, currentPage);
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5f);
                    negativeButton.setLayoutParams(params);
                    positiveButton.setLayoutParams(params);

                    negativeButton.invalidate();
                    positiveButton.invalidate();
                }
            });
            if (alertDialog != null && !alertDialog.isShowing())
                alertDialog.show();
        }
    }
}
