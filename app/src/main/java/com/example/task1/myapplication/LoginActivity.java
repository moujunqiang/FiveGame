package com.example.task1.myapplication;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task1.myapplication.db.UserDao;
import java.io.IOException;


public class LoginActivity extends AppCompatActivity {

    private UserDao userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userdao = new UserDao(this);

        playbgmRing();
        //将布局好的登录界面activity_main2赋给Main2Activity
        setContentView(R.layout.activity_main2);
        Button logIn = (Button) findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取用户名输入框这个控件
                EditText editUsername = (EditText) findViewById(R.id.userID);
                //将用户向输入框输入的用户名提取出来保存于nameInLogin
                final String nameInLogin = editUsername.getText().toString();
                //定义一个页面跳转的意图
                final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //并向游戏页面传递用户名nameInLogin
                intent.putExtra("editUsername", nameInLogin);
                //获取密码输入框这个控件
                EditText editPassword = (EditText) findViewById(R.id.password);
                //将用户向输入框输入的密码提取出来保存于passwordInLogin
                String passwordInLogin = editPassword.getText().toString();
                //并向游戏页面传递密码passwordInLogin
                intent.putExtra("editPassword", passwordInLogin);
                Cursor cursor = userdao.query(nameInLogin, passwordInLogin);
                if (cursor.moveToNext()) {
                    getSharedPreferences("user", MODE_PRIVATE).edit().putString("username", nameInLogin).commit();
                    cursor.close();

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "密码验证失败，请重新验证登录", Toast.LENGTH_SHORT).show();
                }



            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public AssetManager assetManager;

    public MediaPlayer playbgmRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("backgroundMusic.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }


}
