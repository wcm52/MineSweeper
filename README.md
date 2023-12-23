# 1.总体设计

本次设计主要实现了安卓端的扫雷小游戏，除了实现基本的扫雷玩法之外，还添加了模式选择、自定义扫雷数目和画布大小以及玩法说明等功能。主要设计思路如下：

![扫雷](https://s2.loli.net/2023/12/21/Mdhje1Vg6XD2YL8.png)



![扫雷 (1)](https://s2.loli.net/2023/12/21/z9Nh5sQ4F3SZBPj.png)



# 2.界面设计

## 2.1 游戏界面

### 2.1.1 界面详情

游戏界面最上方包含计时器、笑脸（暂停）、地雷数目。

每点击一下方块，笑脸表情发生一次变化，长按可以标记地雷位置。

为了增加趣味性，在最下方添加了提示按钮，分别是

- 白块全出：一键显示所有没有数字的方块。
- 随机一个：随机显示一个未被点击、不是地雷的方块。
- 获取提示：将有地雷的方块标上小黑点，持续时间1秒，1秒后提示消失。

点击笑脸之后计时器暂停，弹出自定义dialog，可以选择继续、再来一局、返回主菜单；在点击返回主菜单后弹出对话框是否确认退出，如果点击确认，则返回到主菜单。

![image-20231221203437859](https://s2.loli.net/2023/12/21/dpfsrMDJKRmEBv1.png)

### 2.1.2 暂停

点击暂停后可选择继续、再来一局、返回主菜单

![image-20231221201820767](https://s2.loli.net/2023/12/21/xd9T5f2FjEPNYpm.png)

点击返回主菜单后弹出是否确认对话框，点击确认后返回主菜单。

![image-20231221203139024](https://s2.loli.net/2023/12/21/SKTyusFJDCWj59a.png)



### 2.1.3 获取提示

点击获取提示后埋有地雷的方块会显示一个小黑点，持续1s后消失，同时笑脸的表情变为小丑表情。

![image-20231221202053457](https://s2.loli.net/2023/12/21/gFNcqZDK5hUxEo6.png)

### 2.1.4 游戏胜利

将所有没有地雷的方块全部翻开后游戏胜利。

胜利后显示放烟花的动画，笑脸表情变为戴墨镜表情，持续2s后跳转到游戏胜利界面。

![image-20231221202502690](https://s2.loli.net/2023/12/21/A4hFUcNBigvRJ2X.png)

游戏胜利界面会显示剩余地雷数目和总的地雷数量、用时，可以选择再来一局和返回模式选择。

![image-20231221203857545](https://s2.loli.net/2023/12/21/KHzQVnX25NRdFJy.png)



### 2.1.5 游戏失败

当点击到埋有地雷的方块时，游戏结束。

失败后所有未被插旗和插旗错误的地雷全部显示爆炸动画，被点击的那颗地雷变为红色，笑脸表情变为哭脸，持续1s后跳转到游戏失败页面；

![image-20231221202647007](https://s2.loli.net/2023/12/21/QPApKyNgCTxV3fR.png)

游戏失败和游戏胜利界面基本一致，会显示剩余地雷数量和总的地雷数量、用时，可以选择再来一局和返回模式选择。

![img](https://s2.loli.net/2023/12/21/cdZxmhYUnf7CaOP.png)

## 2.2 主菜单

主菜单界面包括模式选择、玩法说明和退出游戏三个选项，点击模式选择跳转到模式选择界面，点击玩法说明跳转到玩法说明界面，点击退出游戏直接退出。

![image-20231221195544629](https://s2.loli.net/2023/12/21/G1t82Y9qzhyOKQJ.png)

## 2.3 模式选择

模式选择包括初级、中级、高级、地狱和自定义五种模式，可以任意选择一个模式开始游戏。

![image-20231221195605840](https://s2.loli.net/2023/12/21/F58dSyibpUY14WE.png)

### 2.3.1 四种级别

初级的页面布局为 9*9，总共有10颗地雷

![image-20231221195915100](https://s2.loli.net/2023/12/21/n8y19JVEwZgdKpX.png)

中级的页面布局为 12*9，总共有20颗地雷

![image-20231221200029685](https://s2.loli.net/2023/12/21/sX6VfcgPITH1KNd.png)

高级的页面布局为16*9，共有30个地雷

![image-20231221200122237](https://s2.loli.net/2023/12/21/jFS4CisZ1ONh9eo.png)

地狱的页面布局为 20*13，总共有50颗地雷

![image-20231221200200286](https://s2.loli.net/2023/12/21/SLkgtH73Dp4XrJ9.png)

### 2.3.2 自定义

自定义界面由玩家自行输入地雷数目和画布的长和宽，通过 dialog 的形式显示，点击确认后就以玩家刚刚输入的数据生成游戏。

![image-20231221200343140](https://s2.loli.net/2023/12/21/pkbsdQ8cj2wDRJS.png)

## 2.3 玩法说明

玩法说明简要介绍游戏的玩法，帮助玩家更快、更迅速地了解游戏。

![image-20231221200452858](https://s2.loli.net/2023/12/21/lXtw6dv3qN7xGnb.png)

# 3.详细设计

## 3.1 初始化布局

### 3.1.1 初始化画布和地雷

新建一个Mine类，用于存放画布上的每个位置的属性，包括位置id，是否是地雷，是否是空白方块，是否插旗，是否已经点击等，方便后续进行操作，以下是 Mine.class 的主要方法：

```java
//设置地雷
public void setMine(){
    isMine = true;
}
//返回是否是地雷
public boolean getMine(){
    return isMine;
}
//设置地雷id
public void setID(int id){
    ID = id;
}
//获取当前位置Id，用于设置图片
public int getID(){
    return ID;
}
//设置白板，即周围一个地雷也没有
public void setBlank(){
    isBlank = true;
}
//返回是否是白板
public boolean getBlank(){
    return isBlank;
}
//直接点击和白板递归点击
public void setClicked(){
    isClick = true;
}
public boolean getClicked(){
    return isClick;
}
//设置旗帜
public void setFlag(boolean b){
    flag = b;
}
public boolean getFlag(){
    return flag;
}
```

之后在 MainActivity 中初始化画布

```java
public void initNewMine(){
    for(int i = 0; i < width; i++){
        for(int j = 0; j < height; j++){
            mines[i][j] = new Mine();
        }
    }
}
```

随机生成地雷

```java
//初始化地雷
public void initMine(){
    int x;
    int y;
    Random random = new Random();
    for(int i = 0; i < mineCnt; i++){
        do{
            x = random.nextInt(width);
            y = random.nextInt(height);
            Log.d("cnt", String.valueOf(x) + " " + String.valueOf(y));
        }while (mines[x][y].getMine());//如果这个已经设置为地雷了，则重新找一组随机数
        mines[x][y].setMine(); //将这个位置设置为地雷
    }
}
```



在布局方面首先设置好基本布局（游戏界面），也就是 activity_main.xml ，然后动态创建画布上的方块。

### 3.1.2 获得方块大小

为了保证方块在不同的手机上都能显示，先获取一下屏幕的大小，然后方块的宽度设置为（屏幕大小-60）除以水平方块个数（减60是为了在显示是水平方向留出部分空隙），高度设置为屏幕高度除以垂直方块个数，（高度再乘0.75是为了流出时间和提示部分的位置），因为方块是一个正方形，因此最后取二者的最小值作为方块大小

```java
public void getScreenLength(){
    // 获取屏幕的度量信息
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    // 屏幕的实际像素宽度和高度
    screenWidth = displayMetrics.widthPixels;
    screenHeight = displayMetrics.heightPixels;
    //设置地雷的大小属性
    param = Math.min((screenWidth-60) / height,(int)(0.75 * screenHeight/ width));
}
```

### 3.3.3 设置方块图片

先获得 activity_main.xml 文件中存放方块的布局 layout，然后再利用双层循环，

内层循环创建一个水平线性布局，实例化每一个方块图片，在实例化方块图片时给每个方块图片设置 id ，方便后续获得这个方块图片并进行更改。然后将这个图片加入水平布局中，设置每一个方块的点击事件和长按事件；

外层循环在内层循环结束之后将水平布局加入垂直布局当中，这样存放方块的画布就创建好了。

![image-20231221223440557](https://s2.loli.net/2023/12/21/XVRaw4m3I2L7UEO.png)

## 3.2 点击事件

### 3.2.1 点击事件

在点击事件中，先统计游戏目前的持续时间，方便在点到地雷时传到游戏结束页面显示；

根据当前的图片 id 获得画布位置，判断是否结束，这个位置是否插旗，是否是地雷，是否胜利，设置笑脸图片的变化。

![image-20231221225226087](https://s2.loli.net/2023/12/21/qaTQ2PuXDvhrLYB.png)



在点击事件中，每次判断此时点击的位置是否是地雷，如果是地雷则设置失败动画、震动，显示所有地雷并跳转到失败界面。

```java
//如果是地雷
if (mines[x][y].getMine()){
    faceImg = 2;
    showMines(view);//显示所有地雷
    isEnd = true;
    img_item.setImageResource(R.drawable.blood);//将踩到的地雷设为红色
    // 设置震动
    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // 振动 100 毫秒，设置振动模式
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibrationEffect);
    } else {
        // 在旧版本上使用过时的方法
        vibrator.vibrate(100);
    }

    delayJumpToFinish(1050);//跳转到游戏结束界面
}
```



如果不是地雷显示此时方块周围的地雷数目的数字；

![image-20231222154704661](https://s2.loli.net/2023/12/22/fTIExrhtXcZvGJ4.png)

若周围没有地雷时，要递归显示周围的所有连着空白方块以及空白方块旁边一个有数字的方块。

```java
// 判断显示周围连着的空白个数
public void dfs(int x,int y){
    mines[x][y].setClicked();
    ImageView newImg = findViewById(mines[x][y].getID());
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
}
public void blankMine(int x,int y){
    dfs(x,y);
}
```



如果游戏胜利，则设置游戏胜利动画、震动，跳转到游戏结束界面。

```java
if(isWin()){
    isEnd = true;
    faceImg = 3;
    animateFireworks(view);
    // 设置震动
    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // 振动 100 毫秒，设置振动模式
        VibrationEffect vibrationEffect = VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibrationEffect);
    } else {
        // 在旧版本上使用过时的方法
        vibrator.vibrate(100);
    }
    delayJumpToFinish(2000);
}
```

最后设置一下笑脸表情变化。

```java
setFaceImg();
```

笑脸图片随着不同的点击被赋予不同的值

```java
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
```

### 3.2.2 长按事件

长按插旗，先获得当前长按的这个方块的图片 id，根据这个id找到画布中这个方块的属性，如果未被点击过，则标记地雷，同时最上方的地雷数目减一并显示。

```java
img.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        //如果已经结束，则长按事件不再生效
        if(isEnd){
            return true;
        }
        img_item = (ImageView) view;
        int tmp = img_item.getId();
        int x = tmp / height;
        int y = tmp % height;
        //判断是否已经点击过了，如果点击过了，则不能插旗
        if( !mines[x][y].getClicked()){
            if(mines[x][y].getFlag()){
                img_item.setImageResource(R.drawable.blank); //移除小旗
                mines[x][y].setFlag(false);
            }
            else {
                img_item.setImageResource(R.drawable.flag); //插旗
                mines[x][y].setFlag(true);
                flagCnt++;
            }
        }
        showNumbers(); //地雷数目减1并显示对应的数字图片到界面
        return true;
    }
});
```



## 3.3 动画设置

在游戏胜利时显示**烟花动画**，通过实现下载 fireworks.json 的烟花动画资源，然后播放动画，设置监听器执行动画结束之后的操作。

![image-20231222161545114](https://s2.loli.net/2023/12/22/jJ3gVbUeQonfS9h.png)

在点击到地雷后显示爆炸动画，爆炸动画创建方式与烟花动画创建方式一致，但爆炸动画要为每一个地雷创建一个，因此还要获得地雷位置，然后将爆炸动画设置到每一个地雷的位置处。

![image-20231222161938035](https://s2.loli.net/2023/12/22/Gl4MzNo9dR7HQbv.png)

## 3.4 时钟设置

### 3.4.1 计时设置

创建一个计时器，计时器每隔1s刷新一次，每刷新一次更新页面显示时间的图片。

```java
//获取开始计时的系统时间
public void startTimer(){
    timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            updateTimerImg(SystemClock.elapsedRealtime() - startTime);
        }
    };
    startTime = SystemClock.elapsedRealtime(); //获取系统启动的时间（毫秒为单位）
    timer.scheduleAtFixedRate(task, 1000, 1000); //每隔1000ms更新一下，以1000ms的速率重复进行
}
```



### 3.4.2 暂停设置

点击最上方的笑脸之后，时钟暂停计时，同时弹出自定义设置的对话框。

```java
//给笑脸添加点击事件，点击后暂停游戏
LinearLayout layout = findViewById(R.id.face);
layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        timer.cancel(); //停止计时器
        pauseTime += SystemClock.elapsedRealtime() - startTime;
        showCustomDialog();
    }
});
```

由于要统计游戏持续时间，因此在点击暂停之后记录一下之前游戏持续的时间，就用当前的时间减去游戏开始的时间。

游戏的持续时间就暂停开始后的时长加上暂停之前的时间（在点击事件中统计）。

```java
gameDuration = SystemClock.elapsedRealtime() - startTime + pauseTime;
```

自定义对话框通过新建一个对话框接口继承自 Dialog，实现  View.OnClickListener ，MainAcyivity实现这个接口，自定义对话框包含暂停，继续，返回主菜单。点击这些选项之后执行对应的点击事件。

```java
//点击暂停，显示自定义对话框
public void showCustomDialog(){
    CustomDialog dialog = new CustomDialog(this, this);
    dialog.show();
    // 设置点击外部空白处不取消对话框
    dialog.setCanceledOnTouchOutside(false);
}
```



## 3.5 提示设置

### 3.5.1 获取提示

点击获取提示后会所有有地雷的方块会标上小黑点，持续1s后消失。通过创建计时器来实现，点击之后显示提示，1s之后隐藏所有提示。

```java
//获取提示
public void getMinesTips(){
    Button button_tips = findViewById(R.id.tips);
    button_tips.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!tipsOnce){//防止多次点击出现错误，只有一次点击执行结束后才会进行下一次点击
                tipsOnce = true;
                getTips();
                // 获得提示后隔1秒隐藏提示
                timerTips = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        //计时器执行完之后的操作
                        hideTips();
                        tipsOnce = false;
                        cancelTimer(timerTips);
                    }
                };
                timerTips.schedule(task, 1000); //每隔1000ms更新一下
            }
        }
    });
}
```

### 3.5.2 白块全出

点击白块全出后后显示所有空白方块和空白方块周围的数字方块。

```java
public void showBlanks(){
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
```

### 3.5.3 随机一个

点击之后随机显示一个没有地雷，未被插旗，未被点击的方块。一局的随机一个不能点击超过三次，每次点击后会弹窗显示剩余的点击次数。

![image-20231222165514413](https://s2.loli.net/2023/12/22/kJndNprhLiXY4Iz.png)

## 3.6 游戏结束

### 3.9.1 游戏结束跳转

游戏胜利或者游戏失败时会跳转到游戏结束界面，将游戏持续时间、游戏状态、地雷总数目、插旗数目、画布长和宽通过 intent 传递到游戏结束页面。

```java
public void navigateToGameFinish(){
    Intent intent = new Intent(MainActivity.this,GameFinish.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //不显示切换动画
    intent.putExtra("game_duration",gameDuration);
    intent.putExtra("game_status",isWin());
    intent.putExtra("mines_cnt",mineCnt);
    intent.putExtra("flag_cnt",flagCnt);
    intent.putExtra("width",width);
    intent.putExtra("height",height);
    startActivity(intent);
    finish(); // 关闭当前界面
}
```

由于游戏结束后要留出时间显示胜利和失败动画，因此创建一个计时器实现延迟跳转。

```java
//延迟跳转到结束页面
public void delayJumpToFinish(int t){
    if(!timeClicked){
        timeClicked = true;
        Timer time = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeClicked = true;
                cancelTimer(time);
                navigateToGameFinish();
            }
        };
        time.schedule(task, t);//延迟t秒后执行 run 中的内容
    }
}
```

### 3.9.2 游戏结束界面

游戏结束界面包括显示游戏胜利或游戏失败文字，显示游戏时间、地雷数目，这些通过获得游戏界面传递过来的参数进行显示；

![image-20231222171849505](https://s2.loli.net/2023/12/22/tpoRLSl6sGUbIOD.png)



还可以选择再来一局和返回模式选择，如果选择再来一局则将传递过来的画布宽度、高度和地雷数目再传递到游戏界面。

```java
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
```

## 3.7 其他页面

### 3.7.1 主菜单

主菜单界面主要是跳转页面，点击按钮后跳转到相应的页面，点击退出游戏后直接退出游戏。

![image-20231223123121425](https://s2.loli.net/2023/12/23/zV7436AbZML8SdQ.png)

### 3.7.2 模式选择

页面字符串布局设置：设置按钮中的字体样式，包含两行内容，第一行等级，第二行地雷图片和地雷个数。

![image-20231222172414057](https://s2.loli.net/2023/12/22/H1GMk978LgPIcXN.png)

自定义弹窗设置：点击自定义后弹出对话框设置画布宽度、高度和地雷个数，点击确认后跳转到游戏界面。

```java
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
```

### 3.7.3 玩法介绍

新建玩法介绍页面，设置好布局，简要概括游戏玩法。

![image-20231223122219586](https://s2.loli.net/2023/12/23/kaB3fVyQrzTN4ue.png)







