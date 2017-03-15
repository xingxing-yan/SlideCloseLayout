package com.yyx.slidecloselayout.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yyx.library.SlideCloseLayout;
import com.yyx.slidecloselayout.Model;
import com.yyx.slidecloselayout.R;
import com.yyx.slidecloselayout.util.GlideUtils;
import com.yyx.slidecloselayout.widget.TouchImageView;


/**
 * Created by Administrator on 2017/3/15.
 */

public class SlideCloseLayoutActivity extends AppCompatActivity {
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    private SlideCloseLayout mSlideCloseLayout;
    private ImageView mIv;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_close_layout);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        mSlideCloseLayout = (SlideCloseLayout) findViewById(R.id.scl);
        mIv = (ImageView) findViewById(R.id.scl_iv);
        mPager = (ViewPager) findViewById(R.id.scl_pager);

        GlideUtils.loadImage(this, Model.ImgUrls[0], mIv);

        //这个是必要设置的，否则不会有背景的渐变
        mSlideCloseLayout.setGradualBackground(getWindow().getDecorView().getBackground());
        mSlideCloseLayout.setLayoutScrollListener(new SlideCloseLayout.LayoutScrollListener() {
            @Override
            public void onLayoutClosed() {
                onBackPressed();
            }
        });

        CustomPagerAdapter adapter = new CustomPagerAdapter(this);
        mPager.setAdapter(adapter);

    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Model.ImgUrls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            String url = Model.ImgUrls[position];

            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

//            imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
//                @Override
//                public void onMove() {
//                    if (imageView.isZoomed()) {
//                        mSlideCloseLayout.lock();
//                    } else {
//                        mSlideCloseLayout.unLock();
//                    }
//                }
//            });

            GlideUtils.loadImage(mContext, url, imageView);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

}