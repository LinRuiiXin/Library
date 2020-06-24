package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sz.library.R;
import com.sz.library.adapter.BookCaseViewAdapter;
import com.sz.library.data.Books;
import com.sz.library.pojo.Book;
import com.sz.library.transformer.ScaleTransformer;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.book_case_fragment, container, false);
            ViewPager viewPager = view.findViewById(R.id.book_case_view);
            List<Book> data = Books.getData(activity);
            BookCaseViewAdapter adapter = new BookCaseViewAdapter(activity, data);
            viewPager.setAdapter(adapter);
            viewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    48, getResources().getDisplayMetrics()));
            viewPager.setPageTransformer(false, new ScaleTransformer(activity));
        }
        return view;
    }
}
