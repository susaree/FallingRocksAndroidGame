package com.example.fallingrocks;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Daniel on 7/25/2016.
 */
public class Controller extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{

  public  float centerX;
  public  float centerY;
  public  float baseRadius;
  public  float hatRadius;
  public Canvas myCanvas;
  public  ControllerListener ControllerCallback;

  public  final int ratio = 5; //The smaller, the more shading will occur

    public static final int STICK_NONE = 0;
    public static final int STICK_UP = 1;
    public static final int STICK_UPRIGHT = 2;
    public static final int STICK_RIGHT = 3;
    public static final int STICK_DOWNRIGHT = 4;
    public static final int STICK_DOWN = 5;
    public static final int STICK_DOWNLEFT = 6;
    public static final int STICK_LEFT = 7;
    public static final int STICK_UPLEFT = 8;
    private float distance = 0, angle = 0;
    private int position_x = 0, position_y = 0, min_distance = 0;

    private boolean touch_state = false;

    public void setupDimensions()
    {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 2;
        hatRadius = Math.min(getWidth(), getHeight()) / 4;
    }

    public Controller(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);

       if(context instanceof ControllerListener)
            ControllerCallback = (ControllerListener) context;

    }

    public Controller(Context context, AttributeSet attributes, int style)
    {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);

      if(context instanceof ControllerListener)
            ControllerCallback = (ControllerListener) context;


    }

    public Controller (Context context, AttributeSet attributes)
    {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);

       if(context instanceof ControllerListener)
            ControllerCallback = (ControllerListener) context;

    }

    public void drawController(float newX, float newY)
    {

        if(getHolder().getSurface().isValid())
        {

            myCanvas = this.getHolder().lockCanvas(); //Stuff to draw
            Paint colors = new Paint();

             myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // Clear the BG
            //First determine the sin and cos of the angle that the touched point is at relative to the center of the Controller
            float hypotenuse = (float) Math.sqrt(Math.pow(newX - centerX, 2) + Math.pow(newY - centerY, 2));
            float sin = (newY - centerY) / hypotenuse; //sin = o/h
            float cos = (newX - centerX) / hypotenuse; //cos = a/h

            //Draw the base first before shading
            colors.setARGB(255, 100, 100, 100);
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            for(int i = 1; i <= (int) (baseRadius / ratio); i++)
            {
                colors.setARGB(150/i, 255, 0, 0); //Gradually decrease the shade of black drawn to create a nice shading effect
                myCanvas.drawCircle(newX - cos * hypotenuse * (ratio/baseRadius) * i,
                        newY - sin * hypotenuse * (ratio/baseRadius) * i, i * (hatRadius * ratio / baseRadius), colors); //Gradually increase the size of the shading effect
            }

            //Drawing the Controller hat
            for(int i = 0; i <= (int) (hatRadius / ratio); i++)
            {
                colors.setARGB(255, (int) (i * (255 * ratio / hatRadius)), (int) (i * (255 * ratio / hatRadius)), 255); //Change the Controller color for shading purposes
                myCanvas.drawCircle(newX, newY, hatRadius - (float) i * (ratio) / 2 , colors); //Draw the shading for the hat
            }

            getHolder().unlockCanvasAndPost(myCanvas); //Write the new drawing to the SurfaceView
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.joystickLeft);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
        setupDimensions();
        drawController(centerX, centerY);





    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouch(View v, MotionEvent e)
    {
        if(v.equals(this))
        {
            position_x = (int) (e.getX() - centerX);
            position_y = (int) (e.getY() - centerY);
            distance = (float) Math.sqrt(Math.pow(position_x, 2) + Math.pow(position_y, 2));
            angle = (float) cal_angle(position_x,position_y);

            if(e.getAction() != e.ACTION_UP)
            {
                touch_state = true;
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if(displacement < baseRadius)
                {
                    drawController(e.getX(), e.getY());
                    ControllerCallback.onControllerMoved((e.getX() - centerX)/baseRadius, (e.getY() - centerY)/baseRadius, getId());
                }
                else
                {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawController(constrainedX, constrainedY);
                    ControllerCallback.onControllerMoved((constrainedX-centerX)/baseRadius, (constrainedY-centerY)/baseRadius, getId());
                }
            }
            else
                drawController(centerX, centerY);
            touch_state = false;
            ControllerCallback.onControllerMoved(0,0,getId());
        }
        return true;
    }

    private double cal_angle(float x, float y) {
        if(x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }

    public int get8Direction() {
        if(distance > min_distance && touch_state) {
            if(angle >= 247.5 && angle < 292.5 ) {
                return STICK_UP;
            } else if(angle >= 292.5 && angle < 337.5 ) {
                return STICK_UPRIGHT;
            } else if(angle >= 337.5 || angle < 22.5 ) {
                return STICK_RIGHT;
            } else if(angle >= 22.5 && angle < 67.5 ) {
                return STICK_DOWNRIGHT;
            } else if(angle >= 67.5 && angle < 112.5 ) {
                return STICK_DOWN;
            } else if(angle >= 112.5 && angle < 157.5 ) {
                return STICK_DOWNLEFT;
            } else if(angle >= 157.5 && angle < 202.5 ) {
                return STICK_LEFT;
            } else if(angle >= 202.5 && angle < 247.5 ) {
                return STICK_UPLEFT;
            }
        } else if(distance <= min_distance && touch_state) {
            return STICK_NONE;
        }
        return 0;
    }

    public float getAngle() {
        if(distance > min_distance && touch_state) {
            return angle;
        }
        return 0;
    }

    public interface ControllerListener
    {
        void onControllerMoved(float xPercent, float yPercent, int id);


    }






}