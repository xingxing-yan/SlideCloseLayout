package com.yyx.slidecloselayout.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.yyx.library.SlideCloseLayout;
import com.yyx.slidecloselayout.Model;
import com.yyx.slidecloselayout.R;
import com.yyx.slidecloselayout.util.GlideUtils;

public class MainActivity extends AppCompatActivity {
    private FrameLayout mFl;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFl = (FrameLayout) findViewById(R.id.main_fl);
        mIv = (ImageView) findViewById(R.id.main_iv);

        GlideUtils.loadImage(this, Model.ImgUrls[0], mIv);

        mFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SlideCloseLayoutActivity.class);
            }
        });
    }

    private void gotoActivity(Class<?> clazz){
        Intent intent = new Intent(MainActivity.this, SlideCloseLayoutActivity.class);
        startActivity(intent);
    }
}
