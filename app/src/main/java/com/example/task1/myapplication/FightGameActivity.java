package com.example.task1.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.task1.myapplication.game.Game;
import com.example.task1.myapplication.game.GameConstants;
import com.example.task1.myapplication.game.GameView;
import com.example.task1.myapplication.game.Player;


import java.io.IOException;


public class FightGameActivity extends Activity implements OnClickListener {

    private static final String TAG = "FightGameActivity";

    GameView mGameView = null;

    Game mGame;
    Player black;
    Player white;

    private TextView mBlackWin;
    private TextView mWhiteWin;

    private ImageView mBlackActive;
    private ImageView mWhiteActive;

    // Control Button
    private Button restart;
    private Button rollback;
    int totalCount = 0;

    private Handler mRefreshHandler = new Handler() {

        public void handleMessage(Message msg) {
            Log.d(TAG, "refresh action=" + msg.what);
            switch (msg.what) {
                case GameConstants.GAME_OVER:
                    if (msg.arg1 == Game.BLACK) {
                        showWinDialog("黑方胜");
                        playRing();
                        black.win();
                    } else if (msg.arg1 == Game.WHITE) {
                        showWinDialog("白方胜");
                        playRing();
                        white.win();
                    }
                    updateScore(black, white);
                    break;
                case GameConstants.ADD_CHESS:
                    updateActive(mGame);
                    playPutRing();
                    break;
                default:
                    break;
            }
        }

        ;
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fight);
        initViews();
        initGame();
    }

    private void initViews() {
        mGameView = (GameView) findViewById(R.id.game_view);
        mBlackWin = (TextView) findViewById(R.id.black_win);
        mBlackActive = (ImageView) findViewById(R.id.black_active);
        mWhiteWin = (TextView) findViewById(R.id.white_win);
        mWhiteActive = (ImageView) findViewById(R.id.white_active);
        restart = (Button) findViewById(R.id.restart);
        rollback = (Button) findViewById(R.id.rollback);
        restart.setOnClickListener(this);
        rollback.setOnClickListener(this);
    }

    private void initGame() {
        black = new Player(Game.BLACK);
        white = new Player(Game.WHITE);
        mGame = new Game(mRefreshHandler, black, white);
        mGame.setMode(GameConstants.MODE_FIGHT);
        mGameView.setGame(mGame);
        updateActive(mGame);
        updateScore(black, white);
    }

    private void updateActive(Game game) {
        if (game.getActive() == Game.BLACK) {
            mBlackActive.setVisibility(View.VISIBLE);
            mWhiteActive.setVisibility(View.INVISIBLE);
        } else {
            mBlackActive.setVisibility(View.INVISIBLE);
            mWhiteActive.setVisibility(View.VISIBLE);
        }
    }

    private void updateScore(Player black, Player white) {
        mBlackWin.setText(black.getWin());
        mWhiteWin.setText(white.getWin());
    }

    private void showWinDialog(String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setMessage(message);
        b.setPositiveButton(R.string.Continue, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGame.reset();
                mGameView.drawGame();
            }
        });
        b.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart:
                mGame.reset();
                updateActive(mGame);
                updateScore(black, white);
                mGameView.drawGame();
                break;
            case R.id.rollback:
                boolean rollback = mGame.rollback(1);
                if (!rollback) {
                    Toast.makeText(this, "已经不能悔棋了", Toast.LENGTH_SHORT).show();
                }
                updateActive(mGame);
                mGameView.drawGame();
                break;
            default:
                break;
        }

    }

    public AssetManager assetManager;

    public MediaPlayer playRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("congrats.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    public MediaPlayer playPutRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("put.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

}
