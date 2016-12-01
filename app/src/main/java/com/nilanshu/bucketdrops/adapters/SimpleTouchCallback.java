package com.nilanshu.bucketdrops.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Nilanshu on 01-Dec-16.
 */
public class SimpleTouchCallback extends ItemTouchHelper.Callback {
    private SwipeListener mListener;

    public SimpleTouchCallback(SwipeListener listener) {
        mListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.END);//'END' is used instead of 'RIGHT' because in english end is right but in arabic or hebrew end is left. 0 means drag is not allowed. Swipe is allowed in the direction of 'end'.
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {//Called after the swipe has happened.
        mListener.onSwipe(viewHolder.getAdapterPosition());
    }
}
