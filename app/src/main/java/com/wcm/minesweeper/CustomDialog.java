package com.wcm.minesweeper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private DialogClickListener listener;

    public CustomDialog(Context context, DialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        TextView titleTextView = findViewById(R.id.dialogTitle);
        titleTextView.setText("游戏暂停");

        TextView messageTextView = findViewById(R.id.dialogMessage);
        messageTextView.setText("选择操作");

        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnRestart = findViewById(R.id.btnRestart);
        Button btnExit = findViewById(R.id.btnExit);

        btnContinue.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int t = v.getId();
        if(t == R.id.btnContinue){
            listener.onContinueClicked();
        }
        if(t == R.id.btnRestart){
            listener.onRestartClicked();
        }
        if(t == R.id.btnExit){
            listener.onExitClicked();
        }
        dismiss();
    }

    public interface DialogClickListener {
        void onContinueClicked();
        void onRestartClicked();
        void onExitClicked();
    }
}
