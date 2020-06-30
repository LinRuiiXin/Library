package com.sz.library.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Consumer;
import androidx.core.util.Pair;
import androidx.viewpager.widget.PagerAdapter;

import com.sz.library.BookInfoActivity;
import com.sz.library.R;
import com.sz.library.pojo.Book;

import java.util.List;

public class BookCaseViewAdapter extends PagerAdapter {
    private List<Book> data;
    private Activity activity;
    private LayoutInflater inflater;

    public BookCaseViewAdapter(Activity activity, List<Book> list) {
        this.activity = activity;
        this.data = list;
        inflater = LayoutInflater.from(activity);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.book_item, container, false);
        ImageView imageView = view.findViewById(R.id.shared_book_background);
        TextView textView = view.findViewById(R.id.shared_book_name);
        Book book = data.get(position);
        imageView.setImageResource(activity.getResources().getIdentifier("book"+book.getId(),"drawable",activity.getPackageName()));
        textView.setText(book.getName());
        container.addView(view);
        setOnClickListener(view,book);
        return view;
    }

    private void setOnClickListener(View view,Book book) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(activity, BookInfoActivity.class);
            intent.putExtra("book",book);
            View image = view.findViewById(R.id.shared_book_background);
            Pair<View, String> p1 = new Pair<>(image, image.getTransitionName());
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity,p1).toBundle());
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
