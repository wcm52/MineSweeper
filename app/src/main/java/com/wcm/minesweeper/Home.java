package com.wcm.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 开始游戏按钮
        Button btnStartGame = findViewById(R.id.btnStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        // 自定义数量按钮
        Button btnCustomize = findViewById(R.id.btnCustomize);
        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customizeQuantity();
            }
        });

        // 退出游戏按钮
        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitGame();
            }
        });
    }

    private void startGame() {
        // 启动游戏界面的逻辑
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void customizeQuantity() {
        // 启动自定义数量界面的逻辑
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void exitGame() {
        // 退出游戏的逻辑
        finishAffinity(); // 关闭所有相关的 Activity
    }
}
