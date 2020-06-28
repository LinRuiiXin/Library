package com.sz.library.fragment.noLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sz.library.LoginActivity;
import com.sz.library.R;

public class NoLoginBorrowFragment extends Fragment {

    private Activity activity;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.borrow_no_login_fragment, container, false);
            TextView btn = view.findViewById(R.id.nlb_btn);
            btn.setOnClickListener(v->{
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            });
        }
        return view;
    }
}
