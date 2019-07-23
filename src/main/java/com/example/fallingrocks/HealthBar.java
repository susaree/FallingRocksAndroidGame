package com.example.fallingrocks;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class HealthBar  {

    private Paint outerColor;
    private Paint innerColor;
    private int height = 10;

    private int outerWidth = 60;
    private int innerWidth = 40;

    private int x=0;
    private int y=0;



    public HealthBar() {

        outerColor = new Paint();
        outerColor.setColor(Color.RED);

        innerColor = new Paint();
        innerColor.setColor(Color.GREEN);



    }

    public void setValue( int value) {
        innerWidth = outerWidth * value;
    }





    public void draw(Canvas canvas){
        canvas.drawRect(x,y,x+outerWidth,y+height,outerColor);
        canvas.drawRect(x,y,x+innerWidth,y+height,innerColor);
    }


    public int getOuterWidth() {
        return outerWidth;
    }

    public void setOuterWidth(int outerWidth) {
        this.outerWidth = outerWidth;
    }

    public int getInnerWidth() {
        return innerWidth;
    }

    public void setInnerWidth(int innerWidth) {
        this.innerWidth = innerWidth;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

