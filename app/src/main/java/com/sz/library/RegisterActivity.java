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
import com.sz.library.utils.SystemUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_reg_name,et_reg_psw,et_reg_psw_two;
    private Button btn_reg_commit;
    private String userName,psw,psw_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SystemUtils.setStatusBarFullTransparent(this);
        init();
    }

    private void init() {
        et_reg_name = findViewById(R.id.reg_name);
        et_reg_psw = findViewById(R.id.reg_psw);
        et_reg_psw_two = findViewById(R.id.reg_psw_two);
        btn_reg_commit = findViewById(R.id.reg_commit);
        btn_reg_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(psw_two)){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!psw.equals(psw_two)){
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this, "此用户已存在", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    saveRegister(userName,psw);
                    Intent data = new Intent();
                    data.putExtra("userName",userName);
                    setResult(RESULT_OK,data);
                    RegisterActivity.this.finish();
                }
            }
        });
    }

    private void saveRegister(String userName, String psw) {
        String md5Psw = MD5Utils.md5(psw);
        User user = new User();
        user.setName(userName);
        user.setPassword(md5Psw);
        user.save();
    }

    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        List<User> users = DataSupport.where("name=?",userName).find(User.class);
        if (!users.isEmpty()){
            has_userName = true;
        }
        return has_userName;
    }

    private void getEditString() {
        userName = et_reg_name.getText().toString().trim();
        psw = et_reg_psw.getText().toString().trim();
        psw_two = et_reg_psw_two.getText().toString().trim();
    }
}
