package com.sz.library.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import com.sz.library.R;
import com.sz.library.pojo.User;
import com.sz.library.view.PromptLoginDialogView;

import org.litepal.crud.DataSupport;


public class UserUtils {
    /*
    * 返回当前登录用户id
    *   @showDialog:是否显示提示对话框
    * */

    public static int getLoginId(Context context,boolean showDialog) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String singStatusKey = getLoginStatusKey(context);
        int id = sharedPreferences.getInt(singStatusKey, -1);
        if (id == -1) {
            if(showDialog)
                showToLoginCard(context);
        }
        return id;
    }

    //改变用户通知设置
    public static void setUserSetting(Context context,int userId,boolean b){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("UserSetting:"+userId,b);
        edit.apply();
    }


    //判断当前有无用户登录
    public static boolean hasUserLogin(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        int anInt = sharedPreferences.getInt(getLoginStatusKey(context), -1);
        return anInt != -1;
    }

    //获取当前已登录用户 ******前提：已确定有用户登录******
    public static User getUser(Context context){
        int loginId = getLoginId(context, false);
        return DataSupport.where("id=?", String.valueOf(loginId)).findFirst(User.class);
    }

    //获取当前用户通知设置 ******前提：已确定有用户登录******
    public static boolean getUserNotifySetting(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        int loginId = getLoginId(context, false);
        return sharedPreferences.getBoolean("UserSetting:" + loginId, false);
    }

    //显示登录对话框
    private static void showToLoginCard(Context context) {

        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        View promptView = PromptLoginDialogView.getView(context);
        dialog.setContentView(promptView);
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setOnDismissListener(d -> {
            ((ViewGroup) promptView.getParent()).removeView(promptView);
        });
        dialog.show();
    }
    /*
    * 清除登录状态
    * */
    public static void clearLoginStatus(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        String string = context.getString(R.string.sharedPreferenceName);
        return context.getSharedPreferences(string,Context.MODE_PRIVATE);
    }

    private static String getLoginStatusKey(Context context) {
        return context.getString(R.string.loginStatusKey);
    }
}
