package com.sz.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sz.library.R;

import java.util.List;

public class BookCaseViewAdapter extends PagerAdapter {
    private List<Integer> list;
    private Context context;
    private LayoutInflater inflater;

    public BookCaseViewAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.book_item, container, false);
        ImageView imageView = view.findViewById(R.id.book_item_im);
        imageView.setImageResource(list.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
