package com.sz.library.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.sz.library.LoginActivity;
import com.sz.library.R;

public class PromptLoginDialogView {
    private static View view;
    public static View getView(Context context){
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.not_login_dialog, null);
            Button button = view.findViewById(R.id.nld_button);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            });
        }
        return view;
    }
}
