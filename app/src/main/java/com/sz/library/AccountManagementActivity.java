package com.sz.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountManagementActivity extends AppCompatActivity {
    @BindView(R.id.rl_changePsw)
    RelativeLayout changePsw;
    @BindView(R.id.rl_logout)
    RelativeLayout logout;
    @BindView(R.id.account_Toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initComponent();
    }

    private void initComponent() {
        changePsw.setOnClickListener(v->{
            SharedPreferences pageData = getSharedPreferences("pageData",MODE_PRIVATE);
            SharedPreferences.Editor editor = pageData.edit();
            editor.putInt("pageId",1);
            editor.commit();
            Intent intent = new Intent(AccountManagementActivity.this,ChangePswActivity.class);
            startActivity(intent);
        });
        logout.setOnClickListener(v->{
            SharedPreferences loginData = getSharedPreferences("pageData",MODE_PRIVATE);
            SharedPreferences.Editor editor = loginData.edit();
            editor.clear();
            editor.commit();
            android.os.Process.killProcess(android.os.Process.myPid());
        });
    }

}
