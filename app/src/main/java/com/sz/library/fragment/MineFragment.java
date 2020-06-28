package com.sz.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sz.library.R;
import com.sz.library.service.BookOverTimeService;
import com.sz.library.utils.UserUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.currentThread;

public class MineFragment extends Fragment {
    private final BlockingQueue<Boolean> blockingQueue;
    private Activity activity;
    private View view;
    private int loginId;
    private Intent serviceIntent;

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
}
