package com.wcm.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.text.style.ImageSpan;
import android.widget.EditText;

public class Model extends AppCompatActivity {
    private int minesCount = 0;
    private int width = 20;
    private int height = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_model);
        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnMedium = findViewById(R.id.btnMedium);
        Button btnHard = findViewById(R.id.btnHard);
        Button btnHard2 = findViewById(R.id.btnHard2);
        Button btnCustomSettings = findViewById(R.id.btn);
        setTextPattern(" 初级\n ",btnEasy,10);
        setTextPattern(" 中级\n ",btnMedium,20);
        setTextPattern(" 高级\n ",btnHard,30);
        setTextPattern(" 地狱\n ",btnHard2,50);


        btnCustomSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomSettingsDialog();
            }
        });

    }
    public void showCustomSettingsDialog() {
        // 加载自定义布局
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_custom_num, null);

        // 获取布局中的EditText
        EditText editTextMines = dialogView.findViewById(R.id.editTextMines);
        EditText editTextWidth = dialogView.findViewById(R.id.editTextWidth);
        EditText editTextHeight = dialogView.findViewById(R.id.editTextHeight);

        // 创建AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("自定义设置")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 获取用户输入的设置值
                        int minesCustom = Integer.parseInt(editTextMines.getText().toString());
                        int widthCustom = Integer.parseInt(editTextWidth.getText().toString());
                        int heightCustom = Integer.parseInt(editTextHeight.getText().toString());
                        minesCount = minesCustom;
                        width = heightCustom;
                        height = widthCustom;
                        startGame(minesCustom,width,height);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 用户取消操作
                    }
                });

        // 显示AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void onDifficultySelected(View view) {
        int id = view.getId();
        if(id == R.id.btnEasy){
            minesCount = 10;
            width = 9;
            height = 9;
        }
        else if(id == R.id.btnMedium){
            minesCount = 20;
            width = 12;
            height = 9;
        }
        else if(id == R.id.btnHard){
            minesCount = 30;
            width = 16;
            height = 10;
        }
        else if(id == R.id.btnHard2){
            minesCount = 50;
            width = 20;
            height = 13;
        }
        // 启动游戏界面并传递雷的数量
        startGame(minesCount,width,height);
    }


    private void startGame(int minesCount,int width,int height) {
        Intent intent = new Intent(this, MainActivity.class);

        // 启动游戏界面并传递雷的数量
        intent.putExtra("minesCount", minesCount);
        intent.putExtra("minesWidth", width);
        intent.putExtra("minesHeight", height);
        startActivity(intent);
        finish();
    }

    public void setTextPattern(String s,Button btn,int cnt){
        // 创建 SpannableString 以设置不同字体大小和颜色
        String buttonText = s+cnt+"个";

        // 获取图片资源，这里假设图片资源为 ic_mine（自行替换为实际的图片资源）
        Bitmap mineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mine_icon);

        // 调整图片大小，根据实际需求调整大小
        int targetWidth = 65;  // 目标宽度
        int targetHeight = 65; // 目标高度
        Bitmap resizedMineBitmap = Bitmap.createScaledBitmap(mineBitmap, targetWidth, targetHeight, true);
        // 创建 BitmapDrawable
        BitmapDrawable resizedMineDrawable = new BitmapDrawable(getResources(), resizedMineBitmap);

        // 创建 SpannableString
        SpannableString spannableString = new SpannableString(buttonText);

        // 设置相对大小样式
        // RelativeSizeSpan 用于设置文本的相对大小
        // 参数 0.7f 表示文本相对于原始大小的缩放比例，即缩小 30%
        // 参数 buttonText.indexOf("地雷") 表示从字符串中包含"地雷"的位置开始应用样式
        // 参数 buttonText.length() 表示应用样式的范围从指定位置到字符串末尾
        // 参数 Spannable.SPAN_EXCLUSIVE_EXCLUSIVE 表示样式的范围，SPAN_EXCLUSIVE_EXCLUSIVE 意味着样式将应用于起始和结束索引之间的文本，不包括索引位置的文本
        // 设置地雷数量的颜色为灰色
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), buttonText.indexOf("\n"), buttonText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new RelativeSizeSpan(0.7f), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置等级颜色为黑色
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, buttonText.indexOf(s), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 插入调整大小的图片到 SpannableString
        resizedMineDrawable.setBounds(0, 0, targetWidth, targetHeight);
        spannableString.setSpan(new ImageSpan(resizedMineDrawable, ImageSpan.ALIGN_BASELINE), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置 SpannableString 到按钮的 text 属性
        btn.setText(spannableString);
    }

}