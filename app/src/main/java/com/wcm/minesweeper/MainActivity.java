package com.wcm.minesweeper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.IDN;
import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private int width = 13;//宽度
    private int height = 10; //高度
    private LinearLayout layout_item; //每一行的布局
    private ImageView img; //创建的每一块的图片
    private ImageView img_item;//监听事件里的图片（点击的）
    private Mine[][] mines= new Mine[width][height];//创建一个Mine类，用于确定当前位置是否有地雷以及设置地雷
    private int ID = 0;
    private int mineCnt = 35;//设置地雷数量
    private boolean firstClicked = true;
    private boolean isTimerRunning = false; //是否开始计时
    private boolean isEnd = false;
    private long startTime;//开始计时事件
    private Timer timerTips; //显示提示
    private Timer timer;//定时器
    private Timer timerJoker;
    private int faceImg = 0; //设置face的图片，初始是0
    private boolean setTip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTimer();//计时器开始计时
        initAboveNum();//初始化时间和地雷显示
        initNewMine();//定义Mine
        initMine(); //初始化地雷位置
        initBlank();//初始化白板位置
        initLayout();//初始化布局
        initButton();//初始化提示按钮（点击显示提示）
        initShowBlanks();//初始化白块全出按钮（点击后显示全部白块）
//        showMines();
    }

    public void initNewMine(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                mines[i][j] = new Mine();
            }
        }
    }
    public void initShowBlanks(){
        Button show_blanks = findViewById(R.id.show_blank);
        show_blanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < width; i++){
                    for(int j = 0; j < height; j++){
                        if(mines[i][j].getBlank()){
                            blankMine(i,j);
                        }
                    }
                }
            }
        });
    }
    public void initButton(){
        Button button_tips = findViewById(R.id.tips);
        button_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTips();
                timerTips = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        hideTips();
                    }
                };
                timerTips.schedule(task, 1000); //每隔1000ms更新一下

            }
        });
    }
    public void initLayout(){
        //设置布局属性
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(100, 100);
        LinearLayout layout = (LinearLayout) findViewById(R.id.oriLayout);//获取最开始的整体布局
        for(int i = 0; i < width; i++){
            //为每一行创建一个布局
            layout_item = new LinearLayout(this);
            layout_item.setOrientation(LinearLayout.HORIZONTAL); //布局方向是水平的，接下来为每一行添加图片
            for(int j = 0; j < height; j++){
                img = new ImageView(this);

                //设置渐变
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(img, "alpha", 0f, 1f);
                fadeIn.setDuration(800); // 800毫秒的渐变时间
                img.setImageResource(R.drawable.blank);
                fadeIn.start();

                img.setId(ID);
                mines[i][j].setID(ID);
                layout_item.addView(img,lp);
                //长按插旗、取消插旗
                img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //如果已经结束，则长按事件不再生效
                        if(isEnd){
                            return true;
                        }
                        img_item = (ImageView) view;
                        int tmp = img_item.getId();
                        int x = tmp / 10;
                        int y = tmp % 10;
                        //判断是否已经点击过了，如果点击过了，则不能插旗
                        if( !mines[x][y].getClicked()){
                            if(mines[x][y].getFlag()){
                                img_item.setImageResource(R.drawable.blank); //移除小旗
                                mines[x][y].setFlag(false);
                            }
                            else {
                                img_item.setImageResource(R.drawable.flag); //插旗
                                mines[x][y].setFlag(true);
                            }
                        }
                        showNumbers();
                        return true;
                    }
                });
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firstClicked = false;
                        img_item = (ImageView) view;
                        int tmp = img_item.getId();
                        int x = tmp / 10;
                        int y = tmp % 10;
                        //如果已经结束了，则点击事件不再生效
                        if(isEnd){
                            return;
                        }

                        //如果这个位置已经插旗了，则点击事件不生效
                        if(mines[x][y].getFlag()){
                            return;
                        }
                        if (mines[x][y].getMine()){ //如果是地雷
                            faceImg = 2;
                            showMines();//显示所有地雷
                            isEnd = true;
                            img_item.setImageResource(R.drawable.blood);//将踩到的地雷设为红色
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setIcon(R.drawable.icon);
                            builder.setTitle(("你输了！"));
                            builder.setMessage("你踩到了地雷！");
                            builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    recreate();
                                }
                            });
                            builder.setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
//                            builder.setCancelable(false);//显示对话框后点一下对话框不消失，一直存在
                            builder.show();
                        }
                        else{ //如果不是地雷
                            mines[x][y].setClicked();
                            int n = mineNum(x,y);//该位置周围的地雷数量
                            if(n == 0){ //周围一个地雷都没有
                                blankMine(x,y);
                            }
                            else { //周围有地雷
                                mineNumImg(n,img_item);
                            }
                        }
                        if(isWin()){
                            isEnd = true;
                            faceImg = 3;
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                            builder2.setIcon(R.drawable.icon);//系统自带
                            builder2.setTitle(("恭喜你！"));
                            builder2.setMessage("你赢了！！！");

                            builder2.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    recreate();
                                }
                            });
                            builder2.setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                            builder2.show();
                        }
                        //如果结束了，则停止计时
                        if(isEnd){
                            timer.cancel();
                        }
                        setFaceImg();
                    }
                });
                ID++;

            }
            layout.addView(layout_item);
        }

    }

    public void initMine(){
//        int[] x = {0,0,1,3,3,5,7,7,7,8};
//        int[] y = {4,5,7,7,9,2,0,2,8,8};
//        int[] x = {0,1,1,2,3,3,5,6,8,9};
//        int[] y = {6,1,6,8,0,6,6,3,9,1};

//        for(int i = 0; i < 10; i++){
//            mines[x[i]][y[i]].setMine();
//        }
        int x;
        int y;
        Random random = new Random();
        for(int i = 0; i < mineCnt; i++){
            do{
                x = random.nextInt(width);
                y = random.nextInt(height);
                Log.d("cnt", String.valueOf(x) + " " + String.valueOf(y));
            }while (mines[x][y].getMine());//如果这个已经设置为地雷了，则重新找一组随机数
            Log.d("cntMine", String.valueOf(x) + " " + String.valueOf(y));
            mines[x][y].setMine(); //将这个位置设置为地雷
        }

    }
    //设置空白的标志
    public void initBlank(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(mineNum(i,j) == 0 && !mines[i][j].getMine()){
                    mines[i][j].setBlank();
                }
            }
        }
    }

    //初始化最开始的时间和地雷数量
    public void initAboveNum(){
        //初始化地雷数量显示
        int one = 0;
        int ten = 0;
        int hundred = 0;
        one = mineCnt % 10;
        ten = (mineCnt % 100) / 10;
        hundred = (mineCnt % 1000) / 100;
        ImageView img_one = findViewById(R.id.num_one);
        setNumImg(one,img_one);
        ImageView img_ten = findViewById(R.id.num_ten);
        setNumImg(ten,img_ten);
        ImageView img_hundred = findViewById(R.id.num_hundred);
        setNumImg(hundred,img_hundred);
        //初始化时间
        ImageView time_one = findViewById(R.id.time_one);
        setNumImg(0,time_one);
        ImageView time_ten = findViewById(R.id.time_ten);
        setNumImg(0,time_ten);
        ImageView time_hundred = findViewById(R.id.time_hundred);
        setNumImg(0,time_hundred);
        //给笑脸添加点击事件，点击后则重新开始游戏
        LinearLayout layout = findViewById(R.id.face);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel(); //停止计时器
                recreate();
            }
        });
    }
    //统计周围有几颗地雷
    public int mineNum(int a,int b){
        int num = 0;
        //周围的格子关于x,y的增量坐标
        int[] dx = {-1,-1,-1,0,0,1,1,1};
        int[] dy = {-1,0,1,-1,1,-1,0,1};

        for(int i = 0; i < 8; i++){
            int x = a + dx[i];
            int y = b + dy[i];
            if(x < 0 || y < 0 || x >= width || y >= height){
                continue;
            }
            Mine m = mines[x][y];
            if(m.getMine()){
                num++;
            }
        }
        return num;
    }
    //设置周围雷的个数图片
    public void mineNumImg(int n,ImageView img){
        switch (n){
            case 0:
                img.setImageResource(R.drawable.zero);
                break;
            case 1:
                img.setImageResource(R.drawable.first);
                break;
            case 2:
                img.setImageResource(R.drawable.second);
                break;
            case 3:
                img.setImageResource(R.drawable.three);
                break;
            case 4:
                img.setImageResource(R.drawable.four);
                break;
            case 5:
                img.setImageResource(R.drawable.five);
                break;
            case 6:
                img.setImageResource(R.drawable.six);
                break;
            case 7:
                img.setImageResource(R.drawable.seven);
                break;
        }
    }

    //设置时间和个数的图片
    public void setNumImg(int n ,ImageView img){
        switch (n) {
            case 0:
                img.setImageResource(R.drawable.d0);
                break;
            case 1:
                img.setImageResource(R.drawable.d1);
                break;
            case 2:
                img.setImageResource(R.drawable.d2);
                break;
            case 3:
                img.setImageResource(R.drawable.d3);
                break;
            case 4:
                img.setImageResource(R.drawable.d4);
                break;
            case 5:
                img.setImageResource(R.drawable.d5);
                break;
            case 6:
                img.setImageResource(R.drawable.d6);
                break;
            case 7:
                img.setImageResource(R.drawable.d7);
                break;
            case 8:
                img.setImageResource(R.drawable.d8);
                break;
            case 9:
                img.setImageResource(R.drawable.d9);
                break;
        }
    }

    public void blankMine(int x,int y){
        dfs(x,y);

    }

//    判断周围连着的空白个数
    public void dfs(int x,int y){
//        if( mines[x][y].getBlank()){
            mines[x][y].setClicked();
            ImageView newImg = findViewById(mines[x][y].getID());
//            mineNumImg(mineNum(x, y),newImg);
            newImg.setImageResource(R.drawable.zero);//显示白板图片
            //定义上下左右的 x 偏移量
            int[] dx = {-1,-1,-1,0,0,1,1,1};
            int[] dy = {-1,0,1,-1,1,-1,0,1};
            //空白块周围的地雷全部显示
            for(int u = 0; u < 8; u++){
                int ux = x + dx[u];
                int uy = y + dy[u];
                if(ux >= 0 && uy >= 0 && ux < width && uy < height&& !mines[ux][uy].getClicked()){
                    mines[ux][uy].setClicked();
                    if( mines[ux][uy].getBlank()){
                        dfs(ux,uy);//如果是白板，就继续递归，不然就显示地雷数量的图片
                    }
                    ImageView img = findViewById(mines[ux][uy].getID());
                    mineNumImg(mineNum(ux, uy),img);
                }
            }
//        }

    }
    //展示所有地雷
    public void showMines(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                //如果是地雷并且没有插旗，则将图片设置为地雷图片
                if(mines[i][j].getMine() && !mines[i][j].getFlag()){
                    ImageView newImg = findViewById(mines[i][j].getID());
                    newImg.setImageResource(R.drawable.mine);
                }
                //如果不是地雷但被插旗，则显示地雷错误图片
                if(mines[i][j].getFlag() && !mines[i][j].getMine()){
                    ImageView newImg = findViewById(mines[i][j].getID());
                    newImg.setImageResource(R.drawable.mine_error);
                }
            }
        }
    }

    //判断是否胜利
    public boolean isWin(){
        int notClicked = 0;
        boolean isWin = false;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
//                if(mines[i][j].getClicked() || mines[i][j].getClicked2()){
//                    Log.d("Clicked",String.valueOf(i) + ' ' + j);
//                    isClicked++;//统计已点击的数量
//                }
                //如果这个方块没有被点击过但不是地雷，说明没赢
                if(!mines[i][j].getClicked()){
//                    Log.d("isClicked", String.valueOf(i)+ "  " +j);
                    notClicked ++;
                    if(!mines[i][j].getMine()){
//                        Log.d("notClicked", String.valueOf(notClicked));
                        return false;
                    }
                }
            }
        }
//        Log.d("isClicked", String.valueOf(isClicked));
//        if(width * height - isClicked == mineCnt){
//            return true;
//        }
        return true;
    }

    //设置剩余地雷数量的显示
    public void showNumbers(){
        int numSet = 0;
        int nums = 0;
        int one = 0;
        int ten = 0;
        int hundred = 0;
        //显示的地雷数量根据插旗的数量显示
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(mines[i][j].getFlag()){
                    numSet++;
                }
            }
        }
        nums = mineCnt - numSet;
        one = nums % 10;
        ten = (nums % 100) / 10;
        hundred = (nums % 1000) / 100;
        ImageView img_one = findViewById(R.id.num_one);
        setNumImg(one,img_one);
        ImageView img_ten = findViewById(R.id.num_ten);
        setNumImg(ten,img_ten);
        ImageView img_hundred = findViewById(R.id.num_hundred);
        setNumImg(hundred,img_hundred);
    }
    //获取开始计时的系统时间
    public void startTimer(){
        isTimerRunning = true;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateTimerImg(SystemClock.elapsedRealtime() - startTime);
            }
        };
        startTime = SystemClock.elapsedRealtime(); //获取系统启动的时间（毫秒为单位）
        timer.scheduleAtFixedRate(task, 1000, 1000); //每隔1000ms更新一下
    }
    //更新时间图片
    public void updateTimerImg(long timeSeconds){
        int one = 0;
        int ten = 0;
        int hundred = 0;
        int seconds = (int) (timeSeconds / 1000);
        one = seconds % 10;
        ten = (seconds % 100) / 10;
        hundred = (seconds % 1000) / 100;

        ImageView img_one = findViewById(R.id.time_one);
        setNumImg(one,img_one);
        ImageView img_ten = findViewById(R.id.time_ten);
        setNumImg(ten,img_ten);
        ImageView img_hundred = findViewById(R.id.time_hundred);
        setNumImg(hundred,img_hundred);
    }
    //设置笑脸图片
    public void setFaceImg(){
        ImageView imgFace = findViewById(R.id.face_img);
        if(faceImg == 1){
            imgFace.setImageResource(R.drawable.face2);
            faceImg = 0;
        }
        else if(faceImg == 0){
            imgFace.setImageResource(R.drawable.face0);
            faceImg = 1;
        }
        else if(faceImg == 2){ //输了
            imgFace.setImageResource(R.drawable.face3);
        }
        else if(faceImg == 3){//赢了
            imgFace.setImageResource(R.drawable.face4);
        }
        else if(faceImg == 4) {
            //点击提示就设置成小丑图片
            imgFace.setImageResource(R.drawable.joker3);
        }

    }
    //显示提示信息
    public void getTips(){
        //在获得提示之后将笑脸设置为小丑图片
        faceImg = 4;
        setFaceImg();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(mines[i][j].getMine() && !mines[i][j].getClicked() && !mines[i][j].getFlag() && !isEnd){
                    ImageView img = findViewById(mines[i][j].getID());
                    img.setImageResource(R.drawable.hole);
                }
            }
        }
    }
    //隐藏提示信息
    public void hideTips(){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(mines[i][j].getMine()&& !mines[i][j].getFlag() && !isEnd){
                    ImageView img = findViewById(mines[i][j].getID());
                    img.setImageResource(R.drawable.blank);
                }
            }
        }
    }

}