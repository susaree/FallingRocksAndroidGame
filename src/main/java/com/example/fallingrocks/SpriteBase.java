package com.example.fallingrocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public abstract class SpriteBase  {
    private Bitmap image;

    GameView gameView;
    double x,y;
    double dx, dy;
    double health;
    double damage;
    boolean removable = false;

    protected double maxDY;
    protected double minDY;

    protected double gravity;
    protected boolean canJump;
    protected boolean falling = true;
    protected boolean onPlatform = false;


    private double w,h;
    private boolean canScore = true;
    private boolean canDamage = true;

    public SpriteBase(GameView gameView, Bitmap image, double x, double y, double dx, double dy, double health, double damage) {
        this.gameView = gameView;
        this.image = image;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.health = health;
        this.damage = damage;


        this.gravity = 3;
        this.maxDY = 15;
        this.minDY = -35;

        this.w = image.getWidth();
        this.h = image.getHeight();

    }

    public abstract void checkRemovability();

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public boolean isRemovable() {
        return removable;
    }

    public boolean collidesWith(SpriteBase otherSprite){
       return otherSprite.x + otherSprite.getW() >= x && otherSprite.y + otherSprite.getH() >= y && otherSprite.x <= x + getW() && otherSprite.y <= y + getH();
   }


    public boolean collidesWithPlatform(SpriteBase enemySprite){

        if ( x <= enemySprite.x + enemySprite.w/1.5 && y+h <= enemySprite.y + enemySprite.h/2 && x+ w/1.5 >= enemySprite.x && y+h >= enemySprite.y+enemySprite.h/4&& !enemySprite.isRemovable()) {
            canJump = true;
            falling = false;
            onPlatform = true;


            return true;
        } else {
            falling = true;
            onPlatform = false;
            return false;
        }


    }

    protected void fallWithRock(SpriteBase fallingRock){
        if(onPlatform){
            dy=fallingRock.dy;

        }


    }

    public void remove(){
        if(removable){
            this.x=gameView.getWidth();
            this.y=gameView.getHeight();
            this.dx = 0;
            this.dy = 0;
        }

    }
    public boolean collidesWithGround() {

        if(y >= gameView.getHeight()-this.h){

            this.canJump = true;
            this.falling = false;
            return true;
        } else {
            falling = true;
            return false;
        }
    }

    public void fall(){
        if(falling){
            dy+=gravity*2;
            if(dy > maxDY) dy = maxDY;
            onPlatform = false;
            canJump = false;
        }


    }

    protected void jump(double jumpHeight){
        if(canJump){
            dy -= jumpHeight;
            if (dy > minDY) dy = minDY;
            falling = false;

            this.canJump = false;


        }




    }

    protected boolean jumped(){
        if(gameView.jumped){
            onPlatform = false;

            dy = minDY;
            return true;
        }

        return false;
    }





    public void draw(Canvas canvas){
        canvas.drawBitmap(image,(int)x,(int)y,null);
    }

    public void move(){
        x += dx;
        y += dy;
    }

    public String toString(){
        return getClass().getName();
    }



    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getDamage() {
        return damage;
    }


    public double getW() {
        return w;
    }



    public double getH() {
        return h;
    }
    public boolean isCanScore() {
        return canScore;
    }

    public void setCanScore(boolean canScore) {
        this.canScore = canScore;
    }

    public boolean isCanDamage() {
        return canDamage;
    }

    public void setCanDamage(boolean canDamage) {
        this.canDamage = canDamage;
    }


    public void getDamagedBy( SpriteBase otherSprite) {

        if(canDamage){
            health -= otherSprite.getDamage();
        }

    }
    public boolean isAlive() {

        return Double.compare(health, 0) > 0;
    }


}


