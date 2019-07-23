package com.example.fallingrocks;


import android.graphics.Bitmap;

public class Bullet extends SpriteBase {




    public Bullet(GameView gameView, Bitmap image, double x, double y, double dx, double dy, double health, double damage) {
        super(gameView, image, x, y, dx, dy, health, damage);




    }

    @Override
    public void move() {

        super.move();


    }











    @Override
    public void checkRemovability() { }
}

