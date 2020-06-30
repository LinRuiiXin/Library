package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.sz.library.AccountManagementActivity;
import com.sz.library.ChangePswActivity;
import com.sz.library.R;
import com.sz.library.service.BookOverTimeService;
import com.sz.library.utils.UserUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.lang.Thread.currentThread;

public class MineFragment extends Fragment implements View.OnClickListener{
    private final BlockingQueue<Boolean> blockingQueue;
    private Activity activity;
    private View view;
    private int loginId;
    private Intent serviceIntent;
    private DrawerLayout mDrawer;

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
        }
        return view;
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
                Intent intent = new Intent(getContext(),ChangePswActivity.class);
                intent.putExtra("pageData",1);
                startActivity(intent);
            case R.id.rl_logout:
                android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
