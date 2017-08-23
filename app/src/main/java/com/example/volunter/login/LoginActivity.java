package com.example.volunter.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volunter.MainActivity.MainActivity;
import com.example.volunter.R;
import com.example.volunter.db.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dell on 2017/8/22.
 */

public class LoginActivity extends AppCompatActivity {
    private static EditText account;

    private static EditText password;

    private Button login;

    private TextView forgetPassword;

    private TextView signUp;

    private BmobUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //默认初始化Bmob
        Bmob.initialize(this, "0564cb794728b77cfa25d24d525b83f5");

        initView();
        initValue();
    }

    private void initView(){
        //初始化各控件
        account = (EditText) findViewById(R.id.account_login);
        password = (EditText) findViewById(R.id.password_login);
        login = (Button) findViewById(R.id.login);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        signUp = (TextView) findViewById(R.id.sign_up);
        //获取当前用户
        user = BmobUser.getCurrentUser(User.class);
        if (user != null){
            if (user.getEmailVerified()){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            user = new User();
        }

    }

    private void initValue(){
        //初始化控件值
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            account.setText(bundle.getString("account"));
        }else{
            account.setText(user.getUsername());
        }

        //注册登录按钮
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(account.getText().toString());
                user.setPassword(password.getText().toString());
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null){
                            if (user.getEmailVerified()){
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "您的邮箱还未激活", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //注册忘记密码
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        //注册新用户注册
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                account.setText(bundle.getString("account"));
                password.setText(bundle.getString("password"));
                break;
            default:
                break;
        }
    }
}
