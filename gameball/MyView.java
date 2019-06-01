package com.example.hoangle.boucingballgames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MyView extends View implements Runnable {
    private static final String TAG = "MyView";
    public static boolean isOver = false;


    private int x1, y1 = 380, dx1 = 20, dy1 = 20;
    int leftTouch = 40, xTouch, yTouch;
    int min = 100;
    int max = 400;
    int point = 0;

    private Paint paint;
    private Paint paintTextRED, paintTextBlue;
    Bitmap ball, ballResize;
    Random random;


    ArrayList<Brick> lists;
    private SoundManager soundManager;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paintTextRED = new Paint();
        paintTextBlue = new Paint();
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.bal);
        ballResize = Bitmap.createScaledBitmap(ball, 100, 100, false);
        random = new Random();

        x1 = random.nextInt(max - min) + min;

        lists = new ArrayList<Brick>();
        for (int i = 0; i < 10; i++) {
            Brick brick = new Brick(110 * i, 150, 105, 70);
            Brick brick2 = new Brick((110 * i) + 105, 220, 105, 70);
            Brick brick3 = new Brick((110 * i), 290, 105, 70);
            Brick brick4 = new Brick((110 * i) + 105, 360, 105, 70);
            lists.add(brick);
            lists.add(brick2);
            lists.add(brick3);
            lists.add(brick4);
        }
        paintTextRED.setTextSize(70);
        paintTextRED.setColor(Color.RED);
        paintTextBlue.setTextSize(70);
        paintTextBlue.setColor(Color.CYAN);

        soundManager = SoundManager.getInstance();
        soundManager.init(context);


    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();

        int barY = y - 300;
        canvas.drawBitmap(ballResize, x1, y1, null);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        canvas.drawRect(leftTouch, barY, leftTouch + 300, barY + 50, paint);

        canvas.drawText("RESET", 70, 100, paintTextBlue);

        if (x1 > x || x1 < 0) {
//      Log.d(TAG, "onDraw: Touch Screen");
            soundManager.playSound(R.raw.bounce);
            dx1 = -dx1;
        }
        if (y1 > y || y1 < 0) {
            soundManager.playSound(R.raw.bounce);
            dy1 = -dy1;
        }

        if (y1 > barY - 20) {
            if (x1 > leftTouch - 50 && x1 < leftTouch + 350) {
                Log.d(TAG, "onDraw: Touched");
                soundManager.playSound(R.raw.hit_bar);
                dy1 = -dy1;
            } else {
                canvas.drawText("GAME OVER ", (x / 2) - 100, y / 2, paintTextRED);
                soundManager.playSound(R.raw.explosed);
                isOver = true;
            }
        }

        // Draw brick
        for (Brick element : lists) {
            element.drawBrick(canvas, paint);
            if (element.getVisibility()) {
                //kiểm tra ball va chạm với gạch
                if (y1 < element.getY()) {
                    if (x1 > element.getX() && x1 < (element.getX() + element.getWidth())) {
                        Log.d(TAG, "onDraw: touch the brick ");
                        soundManager.playSound(R.raw.hit);
                        element.setInVisible();
                        point += 10;
                        dy1 = -dy1;
                    }
                }
            }
        }

        if (!isOver) {
            invalidate();
        }
        canvas.drawText(String.valueOf(point), x - 100, 100, paintTextBlue);
        update();
    }

    private void update() {
        x1 += dx1;
        y1 += dy1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;


        int actionIndex = event.getActionIndex();


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);


                handled = true;
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);


                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);
                    if (!isOver) {
                        leftTouch = xTouch;
                    }
                }

                // Click on top Left to reset the game
                if (xTouch < 150 && yTouch < 150) {
                    isOver = false;
                    x1 = random.nextInt(max - min) + min;
                    y1 = 380;
                    point = 0;
                    for (Brick element : lists) {
                        element.setVisible();
                    }
                    invalidate();
                }

                handled = true;
                break;

            case MotionEvent.ACTION_UP:

                // invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                //invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:

                handled = true;
                break;

            default:
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }


    @Override
    public void run() {

    }
}
