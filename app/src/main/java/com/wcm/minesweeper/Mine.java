package com.wcm.minesweeper;

public class Mine {
    private int num;
    private boolean isMine = false;
    private int ID;
    private boolean isBlank = false; //周围都没有地雷
    private boolean isClick = false;
    private boolean isClick2 = false;
    private boolean flag = false;
    private boolean tip = false;
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
}
