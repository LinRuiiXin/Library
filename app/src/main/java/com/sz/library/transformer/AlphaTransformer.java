package com.sz.library.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.sz.library.fragment.MineFragment;

public class AlphaTransformer implements ViewPager.PageTransformer{
    private float MINALPHA = 0.5f;
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position<-1||position>1){
            page.setAlpha(MINALPHA);
        }else {
            //不透明->半透明
            if (position<0){
                page.setAlpha(MINALPHA+(1+position)*(1-MINALPHA));
            }else{
                //半透明->不透明
                page.setAlpha(MINALPHA+(1-position)*(1- MINALPHA));
            }
        }
    }
}
