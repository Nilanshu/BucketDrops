package com.nilanshu.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nilanshu.bucketdrops.R;
import com.nilanshu.bucketdrops.beans.Drop;

import io.realm.RealmResults;

/**
 * Created by Nilanshu on 27-Nov-16.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEMS = 0;
    public static final int FOOTER = 1;

    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    private AddListener mAddListener;

    public AdapterDrops(Context context, RealmResults<Drop> results) {
        mInflater = LayoutInflater.from(context);
        update(results);
    }

    public AdapterDrops(Context context, RealmResults<Drop> results, AddListener addListener) {
        mInflater = LayoutInflater.from(context);
        update(results);
        mAddListener = addListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mResults == null || position < mResults.size()) {
            return ITEMS;
        } else {
            return FOOTER;
        }
    }

    public void update(RealmResults<Drop> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    //The viewType in onCreateViewHolder is returned by the function getItemViewType
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        } else {
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.mTextWhat.setText(drop.getWhat());
        }


    }

    @Override
    public int getItemCount() {
        return mResults.size() + 1;//The +1 here specifies that their is a footer in the recyclerview
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        TextView mTextWhat;

        public DropHolder(View itemView) {
            super(itemView);
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button mBtnAdd;
        AddListener mListener;

        public FooterHolder(View itemView, AddListener addListener) {
            super(itemView);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            mBtnAdd.setOnClickListener(this);
            mListener = addListener;
        }

        @Override
        public void onClick(View v) {
            mListener.add();
        }
    }
}
