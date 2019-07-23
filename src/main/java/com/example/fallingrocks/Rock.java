package com.example.fallingrocks;

import android.graphics.Bitmap;

import java.util.Random;

public class Rock extends SpriteBase {

    Random rnd = new Random();

    public Rock(GameView gameView,Bitmap image, double x, double y,  double dx, double dy,  double health, double damage) {
        super(gameView, image, x, y, dx, dy, health, damage);


    }


    public void checkRemovability() {


    }

    @Override
    public void move() {

        dy = rnd.nextDouble() * 1.0 + 5.0;
        super.move();

        // ensure the ship can't move outside of the screen

    }
}
