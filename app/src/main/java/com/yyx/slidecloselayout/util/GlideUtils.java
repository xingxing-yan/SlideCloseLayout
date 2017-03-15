package com.yyx.slidecloselayout.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/3/15.
 */

public class GlideUtils {
    public static void loadImage(Context context, String url, ImageView iv){
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .into(iv);
    }
}
