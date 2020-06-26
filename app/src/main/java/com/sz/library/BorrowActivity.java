package com.sz.library;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.sz.library.pojo.Borrow;
import com.sz.library.utils.SystemUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
* intent内容：
*       用户id---value name : userId
*       图书id---value name : bookId
* */
public class BorrowActivity extends AppCompatActivity {
    @BindView(R.id.ab_toolBar)
    Toolbar toolbar;
    @BindView(R.id.ab_calendar_view)
    CalendarView calendarView;
    @BindView(R.id.ab_calendar_reset)
    TextView reset;
    @BindView(R.id.ab_choice_comp)
    TextView complete;
    private Intent intent;

    private static SimpleDateFormat dateFormatter;
    static {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    private int userId;
    private int bookId;

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
        intent = getIntent();
        userId = intent.getIntExtra("userId",-1);
        bookId = intent.getIntExtra("bookId",-1);
        initToolBar();
        initCalenderView();
        //复位到当前日期
        reset.setOnClickListener(v -> calendarView.setDate(System.currentTimeMillis()));
        //判断所选日期是否在规定之内
        complete.setOnClickListener(completeListener());
    }

    /*
    * 初始化ToolBar
    * */
    public void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
    * 初始化日历视图，设定日期区间，设置点击事件的检查与提示
    * */

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initCalenderView() {
        calendarView.setMinDate(System.currentTimeMillis());
        calendarView.setMaxDate(System.currentTimeMillis()+2592000000l);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            long now = System.currentTimeMillis();
            long backDay = getBackDayTimeMillis(year, month, dayOfMonth);
            calendarView.setDate(backDay);
            long differ = backDay - now;
            String prompt = conditionLength(differ);
            if (prompt != null) {
                Toast.makeText(BorrowActivity.this, prompt, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    * 判断借阅时长是否符合规定，如符合，存入数据表
    * */
    private View.OnClickListener completeListener() {
        return v->{
            long now = System.currentTimeMillis();
            long choiceDate = calendarView.getDate();
            String s = conditionLength(choiceDate - now);
            if(s == null){
                Date borrowDate = new Date(now);
                Date backDate = new Date(choiceDate);
                Borrow borrow = buildBorrow(userId,bookId,borrowDate,backDate);
                if(borrow != null){
                    borrow.save();
                    Toast.makeText(BorrowActivity.this,"借阅成功,请于"+dateFormatter.format(backDate)+"归还",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }else{
                Toast.makeText(BorrowActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        };
    }
    /*
    * 构建借还对象，赋初始值：
    *        borrowDate：借阅起始时间
    *        backDate：约定归还时间
    * */
    private Borrow buildBorrow(int userId, int bookId, Date borrowDate, Date backDate) {
        Borrow borrow = null;
        if(userId != -1 && bookId != -1){
            borrow = new Borrow();
            borrow.setUserId(userId);
            borrow.setBookId(bookId);
            borrow.setReturnBack(false);
            borrow.setReturnDay(null);
            borrow.setBorrowDay(dateFormatter.format(borrowDate));
            borrow.setPromiseDay(dateFormatter.format(backDate));
        }else{
            Toast.makeText(BorrowActivity.this,"异常，请退出重试",Toast.LENGTH_SHORT).show();
        }
        return borrow;
    }
    /*
     * 判断借阅时间
     *   条件：
     *       1.在当前日期之后
     *       2.不超过30天
     *   返回：
     *       提示信息，如为空，则判断通过
     * */

    private String conditionLength(long differ) {
        String prompt = null;
        long daysMillis = 2592000000l;
        if (differ <= 0) {
            prompt = "归还日期不应在当前日期之前";
        } else if (differ > daysMillis) {
            prompt = "借阅时间不能超过30天";
        }
        return prompt;
    }

    /*
     * 获取指定日期当天00:00:00的毫秒数
     * 注意！指定月份应与实际月份-1，如5月则传入4月即可
     * */
    private long getBackDayTimeMillis(int year, int month, int dayOfMonth) {
        /*LocalDateTime endDay = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
        return endDay.toInstant(ZoneOffset.of("+8")).getEpochSecond();*/
        Calendar instance = Calendar.getInstance();
        instance.set(year, month, dayOfMonth);
        return instance.getTimeInMillis();

    }

}
