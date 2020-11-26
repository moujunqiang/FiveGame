package com.example.task1.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;

import static com.example.task1.myapplication.R.id.renjichoice_jiandan;

public class MainActivity extends Activity {
    boolean stop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playbgmRing();
        setContentView(R.layout.activity_main);
        //将检索出来的intent赋值给intentMain变量
        Intent intentMain = getIntent();
        //获取Main2Activity中传递出来的用户名
        String nameInMyView = intentMain.getStringExtra("editUsername");
        //通过id获取人机对战按钮控件
        Button newGame = (Button) findViewById(R.id.new_game);
        //通过id获取双人对战按钮控件
        Button fight = (Button) findViewById(R.id.fight);

        //为人机对战按钮注册监听器
        newGame.setOnClickListener(new OnClickListener() {
            Intent i;

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SingleGameActivity.class));

            }
        });

        //为双人对战按钮注册监听器
        //当该按钮被点击时，跳转到FightGameActivity.class界面
        fight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FightGameActivity.class));
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
