package com.sz.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sz.library.utils.SystemUtils;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SystemUtils.setStatusBarFullTransparent(this);
        ButterKnife.bind(this);
    }
}
