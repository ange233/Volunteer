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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email;

    private Button askForEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
        initValue();
    }

    private void initView(){
        email = (EditText) findViewById(R.id.email_forget_password);
        askForEmail = (Button) findViewById(R.id.ask_for_email);
    }

    private void initValue(){
        askForEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.resetPasswordByEmail(email.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(ForgetPasswordActivity.this, "重置密码成功，请到" + email.getText().toString() + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ForgetPasswordActivity.this, "请求失败，请查看您的邮箱输入是否正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
