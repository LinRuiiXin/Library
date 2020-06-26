package com.sz.library.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.sz.library.LoginActivity;
import com.sz.library.R;

public class PromptLoginDialogView {
    private static View view;
    public static View getView(Activity activity){
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(R.layout.not_login_dialog, null);
            Button button = view.findViewById(R.id.nld_button);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            });
        }
        return view;
    }
}
