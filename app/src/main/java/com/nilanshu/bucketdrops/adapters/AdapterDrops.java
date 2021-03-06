package com.nilanshu.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nilanshu.bucketdrops.AppBucketDrops;
import com.nilanshu.bucketdrops.R;
import com.nilanshu.bucketdrops.beans.Drop;
import com.nilanshu.bucketdrops.extras.Util;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Nilanshu on 27-Nov-16.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final int COUNT_FOOTER = 1;
    public static final int COUNT_NO_ITEMS = 1;
    public static final int ITEMS = 0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER = 2;
    private final ResetListener mResetListener;
    private MarkListener mMarkListener;
    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    private AddListener mAddListener;
    private Realm mRealm;
    private int mFilterOption;
    private Context mContext;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener addListener, MarkListener markListener, ResetListener resetListener) {
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        mResetListener = resetListener;
        mAddListener = addListener;
        mMarkListener = markListener;
        mContext = context;
        update(results);
    }

    @Override
    public int getItemViewType(int position) {
        if (!mResults.isEmpty()) {
            if (position < mResults.size()) {
                return ITEMS;
            } else {
                return FOOTER;
            }
        } else {
            if (mFilterOption == Filter.COMPLETE
                    || mFilterOption == Filter.INCOMPLETE) {
                if (position == 0) {
                    return NO_ITEM;
                } else {
                    return FOOTER;
                }
            } else//getItemViewType will never go to this else condition because there will be no item available to get its view type.
            {
                return ITEMS;
            }
        }
    }

    public void update(RealmResults<Drop> results) {
        mResults = results;
        mFilterOption = AppBucketDrops.load(mContext);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (position < mResults.size()) {
            return mResults.get(position).getAdded();
        }
        return RecyclerView.NO_ID;
    }

    //The viewType in onCreateViewHolder is returned by the function getItemViewType
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        } else if (viewType == NO_ITEM) {
            View view = mInflater.inflate(R.layout.no_item, parent, false);
            return new NoItemsHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view, mMarkListener);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }


    }

    @Override
    public int getItemCount() {
        if (!mResults.isEmpty()) {
            return mResults.size() + COUNT_FOOTER;
        } else {
            if (mFilterOption == Filter.LEAST_TIME_LEFT
                    || mFilterOption == Filter.MOST_TIME_LEFT
                    || mFilterOption == Filter.NONE) {
                return 0;
            } else {
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
        }
    }

    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.deleteFromRealm(position);
            mRealm.commitTransaction();
            notifyItemRemoved(position);
            mResults.isValid();
        }
        resetFilterIfEmpty();
    }

    private void resetFilterIfEmpty() {
        if (mResults.size() == 0 && (mFilterOption == Filter.COMPLETE || mFilterOption == Filter.INCOMPLETE)) {
            mResetListener.onReset();
        }
//        else if(mResults.size()==1 && (mFilterOption == Filter.COMPLETE || mFilterOption == Filter.INCOMPLETE) && mResults.get(0).getAdded() == 0)
//        {
//            mResetListener.onReset();
//        }
    }

    public void markComplete(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }

    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextWhat;
        TextView mTextWhen;
        MarkListener mMarkListener;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView, MarkListener listener) {
            super(itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
            mTextWhen = (TextView) itemView.findViewById(R.id.tv_when);
            AppBucketDrops.setRalewayRegular(mContext, mTextWhat, mTextWhen);
            mMarkListener = listener;
            mItemView = itemView;
        }

        public void setWhat(String what) {
            mTextWhat.setText(what);
        }

        @Override
        public void onClick(View v) {
            mMarkListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            Drawable drawable;
            if (completed) {
                drawable = ContextCompat.getDrawable(mContext, R.color.bg_drop_complete);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
            }
            Util.setBackground(mItemView, drawable);
        }

        public void setWhen(long when) {
            mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context mContext;
        Button mBtnAdd;
        AddListener mListener;

        public FooterHolder(View itemView, AddListener addListener) {
            super(itemView);
            mContext = itemView.getContext();
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            AppBucketDrops.setRalewayRegular(mContext, mBtnAdd);
            mBtnAdd.setOnClickListener(this);
            mListener = addListener;
        }

        @Override
        public void onClick(View v) {
            mListener.add();
        }
    }

    public static class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }
}
