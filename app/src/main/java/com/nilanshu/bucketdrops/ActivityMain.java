package com.nilanshu.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nilanshu.bucketdrops.adapters.AdapterDrops;
import com.nilanshu.bucketdrops.adapters.AddListener;
import com.nilanshu.bucketdrops.adapters.Divider;
import com.nilanshu.bucketdrops.beans.Drop;
import com.nilanshu.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    RealmResults<Drop> mResults;
    Realm mRealm;
    AdapterDrops mAdapter;
    View mEmptyView;
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    private View.OnClickListener mBtnAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            mAdapter.update(mResults);
        }
    };

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    public void getRealmData(View view) {

        Log.d(" log ", " inside getRealmData ");

        //get the configuration

//        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
//
//        //set the real configuration
//        Realm.setDefaultConfiguration(configuration);

        //get an instance of realm
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Drop> allDrops = realm.where(Drop.class).findAll();

        for (Drop drop : allDrops) {

            Log.d(" log ", " what is it: " + drop.getWhat().toString());
            Log.d(" log ", " added : " + drop.getAdded());
            Log.d(" log ", " Completed : " + drop.isCompleted());

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBtnAdd = (Button) findViewById(R.id.btn_add);
        mEmptyView = findViewById(R.id.empty_drops);
        mAdapter = new AdapterDrops(this, mResults, mAddListener);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mBtnAdd.setOnClickListener(mBtnAddListener);

        setSupportActionBar(mToolbar);
        initBackgroundImage();


    }

    @Override
    protected void onStop() {
        mResults.removeChangeListeners();
        super.onStop();
    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }
}
