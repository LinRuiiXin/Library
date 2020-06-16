package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sz.library.R;
import com.sz.library.adapter.BookCaseViewAdapter;
import com.sz.library.transformer.AlphaTransformer;
import com.sz.library.transformer.ScaleTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookCaseFragment extends Fragment {
    private Activity activity;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
         this.activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.book_case_fragment, container, false);
            ViewPager viewPager = view.findViewById(R.id.book_case_view);
//            viewPager.setPageMargin(60);
//            viewPager.setOffscreenPageLimit(3);
//            List<Integer> list=new ArrayList<>();
//            list.add(R.drawable.portrait);
//            list.add(R.drawable.portrait);
//            list.add(R.drawable.portrait);
//            list.add(R.drawable.portrait);
//            list.add(R.drawable.portrait);
//            BookCaseViewAdapter adapter = new BookCaseViewAdapter(this.getContext(),list);
//            viewPager.setAdapter(adapter);
//            viewPager.setPageTransformer(false,new ScaleTransformer());
            List<Integer> list = new ArrayList<>();
            list.add(R.drawable.portrait);
            list.add(R.drawable.portrait);
            list.add(R.drawable.portrait);
            list.add(R.drawable.portrait);
            list.add(R.drawable.portrait);
            list.add(R.drawable.portrait);
            BookCaseViewAdapter adapter = new BookCaseViewAdapter(this.getContext(), list);
            viewPager.setAdapter(adapter);
            viewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    48, getResources().getDisplayMetrics()));
            viewPager.setPageTransformer(false, new ScaleTransformer(this.getContext()));
        }
        return view;
    }
}
