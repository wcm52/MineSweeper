package com.wcm.minesweeper;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Introduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        // 获取 TextView
        TextView textViewRules = findViewById(R.id.textViewRules);

        // 设置扫雷玩法介绍文本
        String rulesText = "扫雷有初级(9*9，10个)、中级(12*9，20个)、高级(16*9，30个)、地狱(20*13，50个)、自定义\n\n" +
                "整个游戏界面由一个个方块组成，点击确定不是雷的方块，长按标记你认为是雷的区域。\n\n" +
                "任意点击其中一个方块，如果该方块下面藏着地雷，则游戏结束；\n\n" +
                "如果点击方块后显示的数字，比如1，表示这个方块四周的8个方块必然有一个藏着地雷。\n\n" +
                "若界面中不是雷的区域全部点开，则游戏胜利.";
        textViewRules.setText(rulesText);
    }
}
