package com.sz.library;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.sz.library.pojo.Borrow;
import com.sz.library.utils.SystemUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowActivity extends AppCompatActivity {
    @BindView(R.id.ab_toolBar)
    Toolbar toolbar;
    @BindView(R.id.ab_calendar_view)
    CalendarView calendarView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);
        SystemUtils.setStatusBarFullTransparent(this);
        ButterKnife.bind(this);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        initToolBar();
        initCalenderView();
    }

    public void initToolBar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v-> finish());
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initCalenderView() {
        calendarView.setOnDateChangeListener((view,year,month,dayOfMonth)->{
            long now = System.currentTimeMillis();
            LocalDateTime dateTime = LocalDateTime.of(year, month+1, dayOfMonth,0,0);
            long choice = dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            System.out.println("   now:"+now);
            System.out.println("choice:"+choice);
            long timeToBack = choice - now;
            String prompt = conditionLength(timeToBack);
            if(prompt != null){
                Toast.makeText(BorrowActivity.this,prompt,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String conditionLength(long timeToBack) {
        final Long mills = 2592000000L;
        String prompt = null;
        if(timeToBack <= 0){
            prompt = "归还时间不应在当前日期之前";
        }else if(timeToBack >=(mills)){
            prompt = "所选日期超过30天";
        }
        return  prompt;
    }
}
