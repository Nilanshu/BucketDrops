package com.nilanshu.bucketdrops.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.nilanshu.bucketdrops.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nilanshu on 29-Nov-16.
 */
public class BucketRecyclerView extends RecyclerView {

    private List<View> mNonEmptyViews = Collections.emptyList();
    private List<View> mEmptyViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            toggleViews();
        }
    };

    //Used to initialize RecyclerView from code
    public BucketRecyclerView(Context context) {
        super(context);
    }

    //Used to initialize RecyclerView from xml
    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //Used to initialize RecyclerView from xml. Lets you define a custom style for RecyclerView
    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void toggleViews() {
        if (getAdapter() != null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty()) {
            if (getAdapter().getItemCount() == 0) {
                //show all the empty views
                Util.showViews(mEmptyViews);

                //hide the RecyclerView
                setVisibility(View.GONE);

                //hide the views meant to be hidden
                Util.hideViews(mNonEmptyViews);
            } else {
                //hiding all the empty views
                Util.hideViews(mEmptyViews);

                //show the RecyclerView
                setVisibility(View.VISIBLE);

                //show the views meant to be hidden
                Util.showViews(mNonEmptyViews);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public void hideIfEmpty(View... views) {
        mNonEmptyViews = Arrays.asList(views);
    }

    public void showIfEmpty(View... emptyViews) {
        mEmptyViews = Arrays.asList(emptyViews);
    }
}
