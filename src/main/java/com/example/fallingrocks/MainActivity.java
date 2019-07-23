package com.example.fallingrocks;


import android.app.Activity;


import android.os.Bundle;


import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends Activity implements Controller.ControllerListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



         Thread thread = new Thread()  {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView scoreView = findViewById(R.id.scoreLabel);
                                GameView gameView = findViewById(R.id.gameView);
                                scoreView.setText(String.valueOf(gameView.score));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    System.out.println("fail");
                }
            }
        };

        thread.start();



    }


    @Override
    public void onControllerMoved(float xPercent, float yPercent, int id) {
        GameView gameView = findViewById(R.id.gameView);
        Controller controller = findViewById(R.id.joystickLeft);
        int direction = controller.get8Direction();
        if(direction == Controller.STICK_UP) {
            gameView.moveUp();
        } if(direction == Controller.STICK_UPRIGHT) {
            gameView.moveUpRight();
        } if(direction == Controller.STICK_RIGHT) {
            gameView.moveRight();
        } if(direction == Controller.STICK_DOWNRIGHT) {
            gameView.moveDownRight();
        } if(direction == Controller.STICK_DOWN) {
            gameView.moveDown();
        } if(direction == Controller.STICK_DOWNLEFT) {
            gameView.moveDownLeft();
        } if(direction == Controller.STICK_LEFT) {
            gameView.moveLeft();
        } if(direction == Controller.STICK_UPLEFT) {
            gameView.moveUpLeft();
        } else if(direction == Controller.STICK_NONE) {
            gameView.stopMove();
        }

    }


}




