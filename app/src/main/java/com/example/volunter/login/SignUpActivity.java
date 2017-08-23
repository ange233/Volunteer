package com.example.volunter.login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.volunter.MainActivity.MainActivity;
import com.example.volunter.R;
import com.example.volunter.db.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText account;

    private EditText password;

    private EditText passwordAgain;

    private EditText email;

    private Button signUp;

    private BmobUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        initValue();
    }

    private void initView(){
        //初始化各控件
        account = (EditText) findViewById(R.id.account_sign_up);
        password = (EditText) findViewById(R.id.password_sign_up);
        passwordAgain = (EditText) findViewById(R.id.password_again_sign_up);
        email = (EditText) findViewById(R.id.email_sign_up);
        signUp = (Button) findViewById(R.id.sign_up);
        //初始化BmobUser
        newUser = new User();
    }

    private void initValue(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(passwordAgain.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "您两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                newUser.setUsername(account.getText().toString());
                newUser.setPassword(password.getText().toString());
                newUser.setEmail(email.getText().toString());
                newUser.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null){
                            Toast.makeText(SignUpActivity.this, "注册成功,请您尽快去邮箱认证吧", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("account", account.getText().toString());
                            intent.putExtra("password", password.getText().toString());
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this, "您的用户名已存在或邮箱不正确或已注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
