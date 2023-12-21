package com.wcm.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameFinish extends AppCompatActivity {
    private long gameDuration; // 从游戏界面传递过来的游戏时长
    private int mineCnt = 0; //传过来的总的地雷数量
    private int flagCnt = 0; //传过来的插旗数量
    private boolean status = false; //传过来的游戏是否胜利状态
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        // 获取从游戏界面传递过来的游戏时长
        Intent intent = getIntent();
        status = intent.getBooleanExtra("game_status",false);
//        Log.d("Status", status + " ");
        //设置游戏状态
        TextView tvGameStatus = findViewById(R.id.status_text);
        if(status){
            tvGameStatus.setText("游戏胜利");
        }
        else{
            tvGameStatus.setText("游戏失败");
        }

        gameDuration = intent.getLongExtra("game_duration", 0);
        // 设置游戏时长文本
        TextView tvGameDuration = findViewById(R.id.tvGameDuration);
        tvGameDuration.setText("用时：" + formatDuration(gameDuration));

        mineCnt = intent.getIntExtra("mines_cnt",0);
        flagCnt = intent.getIntExtra("flag_cnt",0);
        width = intent.getIntExtra("width",0);
        height = intent.getIntExtra("height",0);
        int remainCnt = mineCnt - flagCnt;
        if(remainCnt < 0){
            remainCnt = 0;
        }
        if(status){
            remainCnt = 0;
        }
        //设置扫雷数量
        TextView tvMinesCnt = findViewById(R.id.tvGameCnt);
        tvMinesCnt.setText("剩余数量：" + remainCnt + "/" + mineCnt);

        // 再来一局按钮
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理再来一局的逻辑
                startGameAgain();
            }
        });

        // 返回主菜单按钮
        Button btnMainMenu = findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理返回主菜单的逻辑
                returnToMainMenu();
            }
        });
    }

    private String formatDuration(long durationInMillis) {
        // 格式化时长为分钟和秒
        long seconds = durationInMillis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void startGameAgain() {
        Intent intent = new Intent(GameFinish.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //不显示切换动画
        // 启动游戏界面并传递雷的数量
        intent.putExtra("minesCount", mineCnt);
        intent.putExtra("minesWidth", width);
        intent.putExtra("minesHeight", height);
        startActivity(intent);
        // 关闭当前界面，返回到游戏界面
        finish();
    }

    private void returnToMainMenu() {
        // 处理返回主菜单的逻辑，例如跳转到主菜单界面
        Intent intent = new Intent(this, Model.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //不显示切换动画
        startActivity(intent);
        finish();
    }
}
