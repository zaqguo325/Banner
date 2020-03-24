package com.glh.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class BannerView extends RelativeLayout {
    private Context context;
    private LinearLayout ll_img;
    private ViewPager viewpager;
    private TextView text;

    private int imgSize=0;
    private String textStr="/";
    private List<ImageView> navigationImg=new ArrayList();
    //导航点
    private int selectColor=getResources().getColor(R.color.colorWhite),noneColor=getResources().getColor(R.color.colorBlack);
    private int imgWidth=1000,imgHeight=30;
    //导航文本
    private int textColor=getResources().getColor(R.color.colorWhite),textBackground=getResources().getColor(R.color.colorLucencyBlack);
    private float textSiz=22;
    //位置
    private List<Integer> listLocation=new ArrayList<>();
    private int top=0,bottom=0,left=0,right=0;
    //默认选中项
    private int selectItem=0;
    //循环间隔时间
    private int time=3000;
    //导航类型
    private int type=1;
    public static int NONE=0;
    public static int DOT=1;
    public static int PAGE=2;

    public BannerView(Context context) {
        super(context);
        this.context = context;
    }
    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.banner_navigation, this);
        ll_img=findViewById(R.id.ll_img);
        viewpager=findViewById(R.id.viewpager);
        text=findViewById(R.id.text);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        if (typedArray != null) {
            //导航点
            imgWidth = (int)typedArray.getDimension(R.styleable.BannerView_navigationWidth, imgWidth);
            imgHeight = (int)typedArray.getDimension(R.styleable.BannerView_navigationHeight, imgHeight);
            selectColor = typedArray.getColor(R.styleable.BannerView_selectColor, selectColor);
            noneColor = typedArray.getColor(R.styleable.BannerView_noneColor, noneColor);
            //导航文本
            textColor = typedArray.getColor(R.styleable.BannerView_textColor, textColor);
            textBackground = typedArray.getColor(R.styleable.BannerView_textBackground, textBackground);
            textSiz=typedArray.getDimension(R.styleable.BannerView_textSize,textSiz);
            //导航位置
            Log.e("-------->>>",""+typedArray.getBoolean(R.styleable.BannerView_navigationAlignParentTop,false));
            if (typedArray.getBoolean(R.styleable.BannerView_navigationAlignParentBottom,false)){
                listLocation.add(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            if (typedArray.getBoolean(R.styleable.BannerView_navigationAlignParentTop,false)){
                listLocation.add(RelativeLayout.ALIGN_PARENT_TOP);
            }
            if (typedArray.getBoolean(R.styleable.BannerView_navigationAlignParentLeft,false)){
                listLocation.add(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            if (typedArray.getBoolean(R.styleable.BannerView_navigationAlignParentRight,false)){
                listLocation.add(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            left=(int)typedArray.getDimension(R.styleable.BannerView_navigationMarginLeft,left);
            top=(int)typedArray.getDimension(R.styleable.BannerView_navigationMarginTop,top);
            right=(int)typedArray.getDimension(R.styleable.BannerView_navigationMarginRight,right);
            bottom=(int)typedArray.getDimension(R.styleable.BannerView_navigationMarginBottom,bottom);
            //默认选中项
            selectItem=typedArray.getInteger(R.styleable.BannerView_currentItem,selectItem);
            //时间
            time=typedArray.getInteger(R.styleable.BannerView_time,time);
            //类型
            type=typedArray.getInteger(R.styleable.BannerView_type,type);

            typedArray.recycle();
        }
        init();
    }


    private Handler hndler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            int currentItem=viewpager.getCurrentItem();
            viewpager.setCurrentItem(currentItem+1);
            hndler.removeCallbacks(this);
            if(time!=0){
                hndler.postDelayed(runnable, time);
            }
        }
    };
    private void init(){
        List<String> list=new ArrayList<>();
        setBannerAdapter(new BannerAdapter<String>(list) {
            @Override
            public void onImg(String item, ImageView img, int position) {
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //设置选中的导航点颜色
                if(position==imgSize+1){
                    viewpager.setCurrentItem(1,false);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    viewpager.setCurrentItem(imgSize,false);
                }else if(position==imgSize+1){

                }else {
                    navigationImg.get(selectItem).setBackground(getGradientDrawable(noneColor));
                    navigationImg.get(position-1).setBackground(getGradientDrawable(selectColor));
                    selectItem=position-1;
                    text.setText(position+textStr);
                    if(onItemChangeListener!=null){
                        onItemChangeListener.onItemChange(position);
                    }
                    Log.e("----->>>滑动",""+selectItem);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置导航类型
        switch (this.type){
            case 0:
                text.setVisibility(GONE);
                ll_img.setVisibility(GONE);
                break;
            case 1:
                text.setVisibility(GONE);
                ll_img.setVisibility(VISIBLE);
                break;
            case 2:
                text.setVisibility(VISIBLE);
                ll_img.setVisibility(GONE);
                break;
        }
        //设置文字
        text.setTextSize(textSiz);
        text.setTextColor(textColor);
        text.setBackground(getGradientDrawable(textBackground));
        RelativeLayout.LayoutParams textLp;
        if(listLocation.size()>0){
            textLp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            for(int location:listLocation){
                textLp.addRule(location);
            }
        }else {
            textLp=(LayoutParams)text.getLayoutParams();
        }
        textLp.setMargins(left,top,right,bottom);
        text.setLayoutParams(textLp);
        //设置导航点
        RelativeLayout.LayoutParams imglp;
        if(listLocation.size()>0){
            imglp=new RelativeLayout.LayoutParams(imgWidth,imgHeight);
            for(int location:listLocation){
                imglp.addRule(location);
            }
            ll_img.setLayoutParams(imglp);
        }else {
            imglp= (LayoutParams) ll_img.getLayoutParams();
            imglp.width=imgWidth;
            imglp.height=imgHeight;
        }
        imglp.setMargins(left,top,right,bottom);
        ll_img.setLayoutParams(imglp);





    }

    /**
     * 初始化Banner
     * @param adapter
     */
    private BannerAdapter adapter;
    public void setBannerAdapter(BannerAdapter adapter){
        this.adapter=adapter;
        this.adapter.setOnItemClickListener(onItemClickListener);
        viewpager.setAdapter(adapter);
        imgSize=adapter.getSize();
        navigationImg.clear();
        for(int i=0;i<imgSize;i++){
            //间隔RelativeLayout
            RelativeLayout rl_img=(RelativeLayout) View.inflate(context,R.layout.banner_navigation_item,null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            //显示用Img
            ImageView img=rl_img.findViewById(R.id.img);
            RelativeLayout.LayoutParams imglp=new RelativeLayout.LayoutParams(imgHeight,imgHeight);
            img.setLayoutParams(imglp);
            //设置背景颜色
            img.setBackground(getGradientDrawable(noneColor));
            navigationImg.add(img);
            //最外层外层
            ll_img.addView(rl_img,lp);
        }
        Log.e("----->>>selectItem",imgSize+""+selectItem);
        if (imgSize>0){
            viewpager.setCurrentItem(selectItem+1);
            textStr="/";
            textStr=textStr+imgSize;
            text.setText((selectItem+1)+textStr);
        }
        if(time!=0){
            hndler.postDelayed(runnable, time);
        }
    }

    /**
     * 设置导航条图片的颜色
     * @param selectColor 选中的颜色
     * @param noneColor 未选中的颜色
     */
    public void setNavigationDotColor(int selectColor, int noneColor){
        this.selectColor=getResources().getColor(selectColor);
        this.noneColor=getResources().getColor(noneColor);
        //设置未选中的颜色
        for(int i=0;i<navigationImg.size();i++){
            if(i==0){
                navigationImg.get(i).setBackground(getGradientDrawable(this.selectColor));
            }else {
                navigationImg.get(i).setBackground(getGradientDrawable(this.noneColor));
            }
        }
    }
    /**
     * 设置导航条图片的颜色
     * @param selectColor 选中的颜色
     * @param noneColor 未选中的颜色
     */
    public void setNavigationDotColor(String selectColor, String noneColor){
        this.selectColor=Color.parseColor(selectColor);
        this.noneColor=Color.parseColor(noneColor);
        //设置未选中的颜色
        for(int i=0;i<navigationImg.size();i++){
            if(i==0){
                navigationImg.get(i).setBackground(getGradientDrawable(this.selectColor));
            }else {
                navigationImg.get(i).setBackground(getGradientDrawable(this.noneColor));
            }
        }
    }

    /**
     * 设置导航条的大小
     * @param width 宽度
     * @param height 高度
     */
    public void setNavigationSize(int width,int height){
        this.imgWidth=width;
        this.imgHeight=height;
        //设置导航条大小
        RelativeLayout.LayoutParams llLp= (LayoutParams) ll_img.getLayoutParams();
        llLp.width=this.imgWidth;
        llLp.height=this.imgHeight;
        ll_img.setLayoutParams(llLp);
        RelativeLayout.LayoutParams imglp=new RelativeLayout.LayoutParams(imgHeight,imgHeight);
        for(ImageView img:navigationImg){
            img.setLayoutParams(imglp);
        }
    }

    /**
     * 设置导航文字
     * @param textSize 字体大小
     * @param textColor 字体颜色
     * @param textBackground 字体背景
     */
    public void setNavigationText(int textSize,int textColor,int textBackground){
        this.textSiz=textSize;
        this.textColor=getResources().getColor(textColor);
        this.textBackground=getResources().getColor(textBackground);
        text.setTextSize(this.textSiz);
        text.setTextColor(this.textColor);
        text.setBackground(getGradientDrawable(this.textBackground));
    }
    /**
     * 设置导航文字
     * @param textSize 字体大小
     * @param textColor 字体颜色
     * @param textBackground 字体背景
     */
    public void setNavigationText(int textSize,String textColor,String textBackground){
        this.textSiz=textSize;
        this.textColor=Color.parseColor(textColor);
        this.textBackground=Color.parseColor(textBackground);
        text.setTextSize(this.textSiz);
        text.setTextColor(this.textColor);
        text.setBackground(getGradientDrawable(this.textBackground));
    }


    /**
     * 设置导航的位置
     * @param locations
     */
    public void setNavigationLocation(int... locations){
        //文字
        RelativeLayout.LayoutParams textLp= (LayoutParams) text.getLayoutParams();
        for(int location:locations){
            textLp.addRule(location);
        }
        text.setLayoutParams(textLp);
        //导航点
        RelativeLayout.LayoutParams imgLp= (LayoutParams) ll_img.getLayoutParams();
        for(int location:locations){
            imgLp.addRule(location);
        }
        ll_img.setLayoutParams(imgLp);
    }

    /**
     * 设置导航栏边距
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setNavigationMargin(int left,int top,int right,int bottom){
        //文字
        RelativeLayout.LayoutParams textLp= (LayoutParams) text.getLayoutParams();
        textLp.setMargins(left,top,right,bottom);
        text.setLayoutParams(textLp);
        //导航点
        RelativeLayout.LayoutParams imgLp= (LayoutParams) ll_img.getLayoutParams();
        imgLp.setMargins(left,top,right,bottom);
        ll_img.setLayoutParams(imgLp);
    }

    /**
     * 设置自动滚动时间
     * @param time time为0，不滚动
     */
    public void setTime(int time){
        this.time=time;
        if(time==0){
            hndler.removeCallbacks(runnable);
        }
    }

    /**
     * 设置导航栏的类型
     * @param type
     */
    public void setNavigationType(int type){
        this.type=type;
        switch (this.type){
            case 0:
                text.setVisibility(GONE);
                ll_img.setVisibility(GONE);
                break;
            case 1:
                text.setVisibility(GONE);
                ll_img.setVisibility(VISIBLE);
                break;
            case 2:
                text.setVisibility(VISIBLE);
                ll_img.setVisibility(GONE);
                break;
        }
    }
    public void setCurrentItem(int position){
        viewpager.setCurrentItem(position+1);
        selectItem=position;
        text.setText((position+1)+textStr);
    }
    public int getCurrentItem(){
        return selectItem;
    }



    //绘制背景
    private Drawable getGradientDrawable(int color){
        GradientDrawable drawable=new GradientDrawable();
        drawable.setCornerRadius(1000);//圆角
        drawable.setColor(color);
        return drawable;
    }
    //view销毁时调用，清空定时任务
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hndler.removeCallbacks(runnable);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemChangeListener onItemChangeListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
        this.adapter.setOnItemClickListener(onItemClickListener);
    }
    public void setOnChangeListener(OnItemChangeListener onChangeListener){
        this.onItemChangeListener=onChangeListener;
    }
    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }
    //-------------------------------------------
    public interface OnItemClickListener{
        void onItemClick(int posion);
    }
    public interface OnItemChangeListener{
        void onItemChange(int posion);
    }
}
