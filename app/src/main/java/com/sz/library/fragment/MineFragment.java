package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.sz.library.ChangePswActivity;
import com.sz.library.LoginActivity;
import com.sz.library.R;
import com.sz.library.pojo.Borrow;
import com.sz.library.pojo.User;
import com.sz.library.service.BookOverTimeService;
import com.sz.library.utils.UserUtils;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.activeCount;
import static java.lang.Thread.currentThread;

public class MineFragment extends Fragment implements View.OnClickListener{
    private final BlockingQueue<Boolean> blockingQueue;
    private Activity activity;
    private View view;
    private int loginId;
    private Intent serviceIntent;
    private DrawerLayout mDrawer;
    private TextView userName;
    private TextView borrowNum;
    private TextView outTimeNum;
    private TextView historyNum;
    private SimpleDateFormat dateFormatter;

    public MineFragment(){
        blockingQueue = new LinkedBlockingQueue<>();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        loginId = UserUtils.getLoginId(activity, false);
        serviceIntent = new Intent(activity, BookOverTimeService.class);
        //防止频繁改变设置
        new Thread(()->{
            while (!currentThread().isInterrupted()){
                try {
                    boolean take = blockingQueue.take();
                    UserUtils.setUserSetting(activity,loginId,take);
                    if(take){
                        activity.startService(serviceIntent);
                    }else{
                        activity.stopService(serviceIntent);
                    }
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mine_fragment, container, false);
            boolean setting = UserUtils.getUserNotifySetting(activity);
            Switch option = view.findViewById(R.id.mf_switch);
            option.setChecked(setting);
            option.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                doCheckingChange(isChecked);
            }));
            if(setting)
                activity.startService(serviceIntent);
            RelativeLayout rl_account = view.findViewById(R.id.mine_account);
            //你把你要的控件名字换掉view就行
            rl_account.setOnClickListener(this);
            mDrawer = view.findViewById(R.id.mine_drawer);
            RelativeLayout changePsw = view.findViewById(R.id.rl_changePsw);
            changePsw.setOnClickListener(this);
            RelativeLayout logout = view.findViewById(R.id.rl_logout);
            logout.setOnClickListener(this);
            init();
        }
        return view;
    }

    private void init() {
        userName = view.findViewById(R.id.mine_username);
        borrowNum = view.findViewById(R.id.mine_borrow_num);
        outTimeNum = view.findViewById(R.id.mine_out_time_num);
        historyNum = view.findViewById(R.id.mine_history_num);
        User user = UserUtils.getUser(getContext());
        String username = user.getName();
        userName.setText(username);
        borrowNum.setText(""+getBorrowNum(user));
        historyNum.setText(""+getBorrowNum(user));
        outTimeNum.setText(""+getOutTimeNum(user));
    }

    private Integer getBorrowNum(User user){
        String userId = String.valueOf(user.getId());
        Cursor cursor = DataSupport.findBySQL("select bookId from Borrow where userId = ?",userId);
        return cursor.getCount();
    }

    private Integer getOutTimeNum(User user){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String userId = String.valueOf(user.getId());
        List<Borrow> borrow = DataSupport.where("userId = ? and isReturnBack = ?",userId,"0").find(Borrow.class);
        List<Borrow> outTimeBorrow = new ArrayList<>();
        for (Borrow borrow1:borrow){
            System.out.println(borrow1);
            try {
                Date parse = dateFormatter.parse(borrow1.getPromiseDay());
                long now = System.currentTimeMillis();
                long promiseDayMillis = parse.getTime();
                if (now-promiseDayMillis>0){
                    System.out.println(borrow1);
                    outTimeBorrow.add(borrow1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return outTimeBorrow.size();
    }

    private void doCheckingChange(boolean isChecked) {
        try {
            blockingQueue.put(isChecked);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //控件的id换xxx
            case R.id.mine_account:
                //跳转写这里
                mDrawer.openDrawer(Gravity.RIGHT);
                break;
            case R.id.rl_changePsw:
                ActivityUtils.startActivity(ChangePswActivity.class);
                break;
            case R.id.rl_logout:
                Intent intent1 = new Intent(getContext(),LoginActivity.class);
                startActivity(intent1);
                ActivityUtils.finishAllActivitiesExceptNewest();
                break;
        }
    }

}
