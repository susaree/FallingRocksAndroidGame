package com.example.fallingrocks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;



import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;



    private Bitmap playerBMP = BitmapFactory.decodeResource(getResources(),R.drawable.stickman2);
    private Bitmap ghostImage = BitmapFactory.decodeResource(getResources(),R.drawable.ghost);
    private Bitmap rockImage = BitmapFactory.decodeResource(getResources(),R.drawable.stickman1);
    private Bitmap bulletImage = BitmapFactory.decodeResource(getResources(),R.drawable.bullet);


    boolean jumped = false;
    boolean jumpKeyPress = false;



    public boolean stopMoving = false;
    private boolean drawable = false;
    Random rnd = new Random();

    Player player;
    Bullet bullet;
    HealthBar healthBar;
    double bulletX;
    double bulletY;
    public int score = 0;
    List<Ghost> ghosts = new ArrayList<>();
    List<Rock> rocks = new ArrayList<>();
    List<Bullet> bullets = new ArrayList<>();
    List<Integer> scores = new ArrayList();






    public GameView(Context context){
        super(context);


        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        player = new Player(this,playerBMP,50,50,0,0,100,0);



        setFocusable(true);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);


       player = new Player(this,playerBMP,50,50,0,0,100,0);

        setFocusable(true);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);


        player = new Player(this,playerBMP,50,50,0,0,100,0);

        setFocusable(true);
    }





    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        thread.setRunning(true);
        thread.start();






    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }

    }





    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();


        if(action == MotionEvent.ACTION_DOWN){
            bulletY = player.getY()+player.getH()/2;
            bulletX = player.getX()+player.getW()/2;

            double dx = (e.getX() - bulletX)*0.20;
            double dy = (e.getY() - bulletY)*0.20;
            bullet = new Bullet( this, bulletImage,bulletX,bulletY,dx,dy,0,60);

            bullets.add(bullet);

        }


        return true;
    }







    public void update(){



        player.fall();
        player.collidesWithGround();



        Quadtree quad = new Quadtree(0, new Rect(0,0,this.getWidth(),this.getHeight()));

        List<SpriteBase>allObjects = new ArrayList<>();
        allObjects.addAll(ghosts);
        allObjects.addAll(rocks);
        allObjects.addAll(bullets);
        allObjects.add(player);


        quad.clear();
        for (int i = 0; i < allObjects.size()-1; i++) {
            quad.insert(allObjects.get(i));
        }
        quad.insert(player);
        List<SpriteBase> returnObjects = new ArrayList<>();


        for (int i = 0; i < allObjects.size(); i++) {
            returnObjects.clear();

            quad.retrieve(returnObjects, player);



            for (int x = 0; x < returnObjects.size(); x++) {
                if(player.collidesWith(returnObjects.get(x)) && returnObjects.get(x) instanceof Ghost && !(returnObjects.get(x) instanceof Player)){


                    returnObjects.get(x).setRemovable(true);
                    returnObjects.get(x).remove();
                    player.getDamagedBy(returnObjects.get(x));
                    if(returnObjects.get(x).isRemovable()){
                        ghosts.remove(returnObjects.get(x));
                    }

                }
                if(!player.isAlive()){
                    scores.add(score);
                    score = 0;
                    player.setHealth(100);
                }

                if(player.collidesWithPlatform(returnObjects.get(x)) && returnObjects.get(x) instanceof Rock && !(returnObjects.get(x) instanceof Player) ){

                    if(player.onPlatform && !jumpKeyPress){

                        player.dy = returnObjects.get(x).getDy();


                    }


                }
            }
        }


        for(Ghost ghost:ghosts) {
            for(Bullet bullet:bullets) {

                if(bullet.collidesWith(ghost)){
                    ghost.getDamagedBy(bullet);
                    ghost.checkRemovability();
                    bullet.setRemovable(true);
                    bullet.remove();
                    //bullets.remove(bullet);
                    if(!ghost.isAlive()&&ghost.isCanScore()){
                        score++;
                        ghost.remove();
                        ghost.setCanScore(false);

                    }
                    ghost.setCanDamage(false);
                }
            }
            ghost.setCanDamage(true);
            if(ghost.isRemovable()){
                ghosts.remove(ghost);
            }
        }


        player.move();

        jumped = false;

        if(!player.collidesWithGround()){
            jumped = true;
        }

        jumpKeyPress = false;




        allObjects.clear();



    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        canvas.drawColor(Color.TRANSPARENT);


        if(drawable){
            for(Bullet bullet:bullets){

                bullet.draw(canvas);
                bullet.move();
            }
        }


        if(drawable){
            for(Rock rock:rocks){
                rock.draw(canvas);
                rock.move();
            }
        }

        player.draw(canvas);




        if(drawable){


            for(Ghost ghost:ghosts){
                ghost.draw(canvas);
                ghost.attackPlayer(player);
                ghost.move();
            }

        }




    }






    public void spawnGhosts( boolean random) {

        if( random && rnd.nextInt(Settings.GHOST_SPAWN_RANDOMNESS) != 0) {
            return;
        }

        // image
        Bitmap image = ghostImage;

        // random speed
        double speed = 10;

        // x position range: enemy is always fully inside the screen, no part of it is outside
        // y position: right on top of the view, so that it becomes visible with the next game iteration
        double x = rnd.nextDouble() * (getWidth());
        double y = getHeight();

        // create a sprite
        Ghost ghost = new Ghost(this, image, x, y, 0, 0, 100, 30, speed);

        // manage sprite
        ghosts.add(ghost);
        drawable = true;

    }

    public void spawnRocks( boolean random) {

        if( random && rnd.nextInt(Settings.ROCK_SPAWN_RANDOMNESS) != 0) {
            return;
        }

        // image
        Bitmap image = rockImage;

        // random speed
        double speed = rnd.nextDouble() * 1.0 + 2.0;

        // x position range: enemy is always fully inside the screen, no part of it is outside
        // y position: right on top of the view, so that it becomes visible with the next game iteration
        double x = rnd.nextDouble() * (getWidth() - image.getWidth());
        double y =- image.getHeight();

        // create a sprite
        Rock rock = new Rock(this, image,x,y,0,0,0,0);

        // manage sprite
        rocks.add(rock);
        drawable = true;
    }

    public void moveLeft(){
        stopMoving = false;


        player.setDx(-20);
        player.setDy(0);




    }
    public void moveRight(){
        stopMoving = false;


        player.setDx(20);
        player.setDy(0);



    }
    public void moveUp(){
        jumpKeyPress = true;
        stopMoving = false;
        player.jump(30);
        player.setDx(0);





    }
    public void moveDown(){
        stopMoving = false;
        jumpKeyPress = true;


        player.setDy(30);
        player.setDx(0);



    }
    public void moveUpRight(){
        jumpKeyPress = true;
        stopMoving = false;
        player.jump(30);
        player.setDx(20);


    }
    public void moveUpLeft(){
        jumpKeyPress = true;
        stopMoving = false;
        player.jump(30);
        player.setDx(-20);

    }
    public void moveDownRight(){
        stopMoving = false;
        jumpKeyPress = true;
        player.setDx(20);
        player.setDy(30);


    }
    public void moveDownLeft(){
        stopMoving = false;
        jumpKeyPress = true;
        player.setDx(-20);
        player.setDy(30);


    }
    public void stopMove(){
        if(stopMoving){
            player.setDx(0);
            player.setDy(0);
        }

        stopMoving = true;

    }




}

