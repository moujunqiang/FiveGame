package com.example.task1.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task1.myapplication.db.UserDao;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUserID;
    /**
     * Clear
     */
    private FrameLayout mUsenameLayout;
    private EditText mPassword;
    /**
     * Clear
     */
    private FrameLayout mUsecodeLayout;
    /**
     * 注册
     */
    private Button mLogin;
    private RelativeLayout mLoginLayout;
    private UserDao userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userdao = new UserDao(this);

        initView();
    }

    private void initView() {
        mUserID = (EditText) findViewById(R.id.userID);
        mUsenameLayout = (FrameLayout) findViewById(R.id.usename_layout);
        mPassword = (EditText) findViewById(R.id.password);
        mUsecodeLayout = (FrameLayout) findViewById(R.id.usecode_layout);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        mLoginLayout = (RelativeLayout) findViewById(R.id.login_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login:

                String name = mUserID.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwd = mPassword.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursor = userdao.query(name, pwd);
                if (cursor.moveToNext()) {
                    Toast.makeText(getApplicationContext(), "该用户已被注册，请重新输入", Toast.LENGTH_LONG).show();
                    mUserID.requestFocus();
                } else {
                    userdao.insertUser(name, pwd);
                    cursor.close();
                    Toast.makeText(getApplicationContext(), "用户注册成功，请前往登录", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }

}