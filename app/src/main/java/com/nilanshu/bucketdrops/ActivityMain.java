package com.nilanshu.bucketdrops;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ActivityMain extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = (ImageView) findViewById(R.id.iv_logo);
        Matrix matrix = new Matrix();
        matrix.setRotate(10.0f);
        logo.setImageMatrix(matrix);
    }
}
