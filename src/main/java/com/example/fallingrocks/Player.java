package com.example.fallingrocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends SpriteBase {



    HealthBar healthBar;


    double healthMax = 100;

    private double w,h;


    public Player(GameView gameView, Bitmap image, double x, double y, double dx, double dy, double health, double damage) {

        super(gameView, image,x,y,dx,dy, health, damage);

        this.healthBar = new HealthBar();


        this.w = image.getWidth();
        this.h = image.getHeight();

    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void move() {

        checkBounds();
        super.move();

    }


    @Override
    public void checkRemovability() {

    }


     public void checkBounds() {

        // top
        if( Double.compare( y, 0) < 0) {

            y = 0;

        }

        // bot
        if( y>=gameView.getHeight()-h ) {

            y = gameView.getHeight()-h;
        }


        // left
        if( Double.compare( x, 0) < 0 ) {

            x = 0;
        }

        // right
        if(x>=gameView.getWidth()-w) {

           x=gameView.getWidth()-w;
        }




    }

    public double getRelativeHealth() {
        return getHealth() / healthMax;
    }


    public void setHealthBarPosition(){
        healthBar.setValue((int)getRelativeHealth());

        healthBar.setX((int)getX());
        healthBar.setY((int)getY()+(int)getH()+5);
    }



}
