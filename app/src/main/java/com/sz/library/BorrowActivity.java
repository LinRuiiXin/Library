package com.sz.library;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.sz.library.utils.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowActivity extends AppCompatActivity {
    @BindView(R.id.ab_toolBar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        SystemUtils.setStatusBarFullTransparent(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initToolBar();
    }

    public void initToolBar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v-> finish());
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
