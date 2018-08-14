package com.example.danazone04.danazone;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public final class RecyclerViewUtils {
    private static RecyclerViewUtils mNewInstance;

    /**
     * single ton
     *
     * @return
     */
    public static RecyclerViewUtils Create() {
        if (mNewInstance == null) {
            mNewInstance = new RecyclerViewUtils();
        }
        return mNewInstance;
    }

    /**
     * set up horizontal for recycler view
     *
     * @param context
     * @param recyclerView
     * @return
     */
    public RecyclerView setUpHorizontal(Context context, RecyclerView recyclerView) {
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    /**
     * set up vertical for recycler view
     *
     * @param context
     * @param recyclerView
     * @return
     */
    public RecyclerView setUpVertical(Context context, RecyclerView recyclerView) {
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    public RecyclerView setUpGrid(Context context, RecyclerView recyclerView, int column) {
        RecyclerView.LayoutManager mLayout = new GridLayoutManager(context, column);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }
}
