package com.glh.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BannerAdapter<T> extends PagerAdapter {
    private List<T> listStr=new ArrayList<>();
    public BannerAdapter(List<T> listStr) {
        super();
        if(listStr.size()>0){
            listStr.add(0,listStr.get(listStr.size()-1));
            listStr.add(listStr.get(1));
        }
        this.listStr = listStr;

    }
    @Override
    public int getCount() {
        return listStr.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView img=new ImageView(container.getContext());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        onImg(listStr.get(position),img,position);
        container.addView(img);
        return img;
    }
    public int getSize(){
        return listStr.size()-2;
    }
    public abstract void onImg(T item,ImageView img,int position);

    private BannerView.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(BannerView.OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
}
