# SlideCloseLayout
仿头条多图预览的页面关闭效果

##### 注：此效果是参考[SwipableLayout](https://github.com/SerhatSurguvec/SwipableLayout)项目改进的，效果更加贴近头条的效果。

# 效果图：
![手指滑动退出](https://github.com/xingxing-yan/SlideCloseLayout/blob/master/git/finger2_exit.gif)
![按返回键退出](https://github.com/xingxing-yan/SlideCloseLayout/blob/master/git/back_exit.gif)

## 用法：
1. 在布局文件中直接添加控件：
```
<?xml version="1.0" encoding="utf-8"?>
<com.yyx.library.SlideCloseLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/scl"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <com.yyx.slidecloselayout.widget.TouchViewPager
        android:id="@+id/scl_pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.yyx.slidecloselayout.widget.TouchViewPager>
</com.yyx.library.SlideCloseLayout>
```
SlideCloseLayout继承自FrameLayout，所以可以当FrameLayout使用。

2. 在Activity中的使用：
```Java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_close_layout);
        //设置activity的背景为黑色
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        mSlideCloseLayout = (SlideCloseLayout) findViewById(R.id.scl);
        mPager = (ViewPager) findViewById(R.id.scl_pager);
        //给控件设置需要渐变的背景。如果没有设置这个，则背景不会变化
        mSlideCloseLayout.setGradualBackground(getWindow().getDecorView().getBackground());
        //设置监听，滑动一定距离后让Activity结束
        mSlideCloseLayout.setLayoutScrollListener(new SlideCloseLayout.LayoutScrollListener() {
            @Override
            public void onLayoutClosed() {
                onBackPressed();
            }
        });
        CustomPagerAdapter adapter = new CustomPagerAdapter(this);
        mPager.setAdapter(adapter);
    }
```
通过setGradualBackground方法给SlideCloseLayout设置需要渐变的背景，如果没有设置，则不会有背景的变化。通过setLayoutSrcollListener给SlideCloseLayout设置结束监听，此时应该关闭Activity。

3. 如果想要按返回键时，也能执行SlideCloseLayout的退出动画效果，则需要在onKeyDown()中在返回键按下时执行SlideCloseLayout的exitLayoutAnim(long duration, boolean isFingerScroll)方法，如下：
```
@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            mSlideCloseLayout.exitLayoutAnim(1000, false);
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
```
基本用法就是这样，是不是很简单，SlideCloseLayout控件的具体实现请看源码。
