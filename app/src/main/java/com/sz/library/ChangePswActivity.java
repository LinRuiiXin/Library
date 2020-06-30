package com.sz.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sz.library.pojo.User;
import com.sz.library.utils.MD5Utils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ChangePswActivity extends AppCompatActivity {

    private EditText et_change_psw_two,et_change_psw,et_change_old_psw,et_change_name;
    private Button btn_change_commit;
    private String oldPsw,newPsw,newPsw_two,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        init();
    }

    private void init() {
        et_change_old_psw = findViewById(R.id.change_old_psw);
        et_change_psw = findViewById(R.id.change_psw);
        et_change_psw_two = findViewById(R.id.change_psw_two);
        et_change_name = findViewById(R.id.change_name);
        btn_change_commit = findViewById(R.id.change_commit);
        btn_change_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(ChangePswActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(ChangePswActivity.this, "没有此用户信息", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(oldPsw)){
                    Toast.makeText(ChangePswActivity.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!MD5Utils.md5(oldPsw).equals(readPsw(userName))){
                    Toast.makeText(ChangePswActivity.this, "输入的密码和原密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPsw)){
                    Toast.makeText(ChangePswActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (MD5Utils.md5(newPsw).equals(readPsw(userName))){
                    Toast.makeText(ChangePswActivity.this, "新密码不能跟旧密码一致", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(newPsw_two)){
                    Toast.makeText(ChangePswActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!newPsw.equals(newPsw_two)){
                    Toast.makeText(ChangePswActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(ChangePswActivity.this, "更改密码成功", Toast.LENGTH_SHORT).show();
                    changePsw(MD5Utils.md5(newPsw),userName);
                    Intent PageData = getIntent();
                    if (PageData.getStringExtra("pageData").equals(1)){
                        Intent intent = new Intent(ChangePswActivity.this,AccountManagementActivity.class);
                        startActivity(intent);
                        ChangePswActivity.this.finish();
                    }else{
                        Intent intent = new Intent(ChangePswActivity.this,LoginActivity.class);
                        startActivity(intent);
                        ChangePswActivity.this.finish();
                    }

                }
            }
        });
    }

    private Integer getPageId() {
        SharedPreferences pageData = getSharedPreferences("pageData",MODE_PRIVATE);
        Integer pageId = pageData.getInt("pageId",0);
        return pageId;
    }

    private void changePsw(String newPsw, String userName) {
        List<User> users = DataSupport.where("name=?",userName).find(User.class);
        users.get(0).setPassword(newPsw);
        users.get(0).save();
    }

    private String readPsw(String userName) {
        List<User> users = DataSupport.where("name=?",userName).find(User.class);
        String dataPsw = users.get(0).getPassword();
        return dataPsw;
    }

    private void getEditString() {
        userName = et_change_name.getText().toString().trim();
        oldPsw = et_change_old_psw.getText().toString().trim();
        newPsw = et_change_psw.getText().toString().trim();
        newPsw_two = et_change_psw_two.getText().toString().trim();
    }
    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        List<User> users = DataSupport.where("name=?",userName).find(User.class);
        if (users.isEmpty()){
            has_userName = true;
        }
        return has_userName;
    }
}
