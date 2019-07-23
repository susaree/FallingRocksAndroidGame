package com.example.fallingrocks;

import android.graphics.Bitmap;

public class Ghost extends SpriteBase {



        double speed;







        public Ghost(GameView gameView,Bitmap image, double x, double y, double dx, double dy, double health, double damage, double speed) {
            super(gameView, image, x, y, dx, dy, health, damage);


            this.speed = speed;


        }

    @Override
    public void checkRemovability() {
        if (health<=0){

            setRemovable(true);
        }
    }






        @Override
        public void move() {

            super.move();

            // ensure the ship can't move outside of the screen

        }

        public void attackPlayer(Player player){
            double targetX = player.getX();
            double targetY = player.getY();


            if(y >= targetY) {
                dy = -speed;



            } else if(y <= targetY) {
                dy = speed;

            }


            // horizontal direction
            if(x >= targetX) {

                dx = -speed;

            } else if( x <= targetX) {

                dx = speed;

            }

        }







}
