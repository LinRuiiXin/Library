package com.sz.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sz.library.pojo.User;
import com.sz.library.utils.MD5Utils;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText et_login_name,et_login_psw;
    private TextView tv_reg,tv_change_psw;
    private Button btn_login_commit;
    private String userName;
    private String psw;
    private String md5Psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        et_login_name = findViewById(R.id.login_name);
        et_login_psw = findViewById(R.id.login_psw);
        tv_reg = findViewById(R.id.tv_reg);
        tv_change_psw = findViewById(R.id.tv_change_psw);
        btn_login_commit = findViewById(R.id.login_commit);
        tv_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
        tv_change_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改密码
                Intent intent = new Intent(LoginActivity.this,ChangePswActivity.class);
                startActivity(intent);
            }
        });
        btn_login_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = et_login_name.getText().toString().trim();
                psw = et_login_psw.getText().toString().trim();
                md5Psw = MD5Utils.md5(psw);
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (isExistUserName(userName)){
                    Toast.makeText(LoginActivity.this, "此用户不存在", Toast.LENGTH_SHORT).show();
                    return;
                }else if (md5Psw.equals(readPsw(userName))){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    saveLoginStatus(userName);
                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);
//                    intent.putExtra("isLogin",true);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    return;
                }else if ((!TextUtils.isEmpty(readPsw(userName))&&!md5Psw.equals(readPsw(userName)))){
                    Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void saveLoginStatus(String userName){
        List<User> users = DataSupport.where("name=?", userName).find(User.class);
        User user = users.get(0);
        SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor edit = loginData.edit();
        edit.putInt("signInUserId",user.getId());
    }


    private void saveLoginStatus(boolean status, String userName) {
        SharedPreferences sp = getSharedPreferences("loginData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin",status);
        editor.putString("userName",userName);
        editor.commit();
    }

    private String readPsw(String userName) {
        List<User> users = DataSupport.where("name=?",userName).find(User.class);
        String dataPsw = users.get(0).getPassword();
        return dataPsw;
    }
    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        List<User> users = DataSupport.where("name=?",userName).find(User.class);
        if (users.isEmpty()){
            has_userName = true;
        }
        return has_userName;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (data!=null){
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)){
                et_login_name.setText(userName);
                et_login_name.setSelection(userName.length());
            }
        }
    }
}

