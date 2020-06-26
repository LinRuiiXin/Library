package com.sz.library.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sz.library.LoginActivity;
import com.sz.library.R;
import com.sz.library.view.PromptLoginDialogView;

public class UserUtils {

    public static int getLoginId(Activity activity){
        String spName = activity.getResources().getString(R.string.sharedPreferenceName);
        String singStatusKey = activity.getResources().getString(R.string.loginStatusKey);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(spName, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(singStatusKey, -1);
        if(id == -1){
            showToLoginCard(activity);
        }
        return id;
    }

    private static void showToLoginCard(Activity activity) {

        final Dialog dialog = new Dialog(activity,R.style.DialogTheme);
        View promptView = PromptLoginDialogView.getView(activity);
        dialog.setContentView(promptView);
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setOnDismissListener(d -> {
            ((ViewGroup)promptView.getParent()).removeView(promptView);
        });
        dialog.show();
    }

}
