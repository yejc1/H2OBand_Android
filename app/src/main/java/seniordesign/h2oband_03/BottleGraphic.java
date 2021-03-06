package seniordesign.h2oband_03;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by saumil on 3/15/17.
 */

public class BottleGraphic extends SurfaceView implements SurfaceHolder.Callback {
    private class BottleThread extends Thread implements Runnable {
        final int INTERVAL = 100;

        final int INTERVALS_UNTIL_WAIT = 50;
        final int MAX_WAIT_INTERVALS = 10;
        int interval_counter;

        private final SurfaceHolder sh;

        BottleThread(BottleGraphic bottleGraphic) {
            if(bottleGraphic != null)
                sh = bottleGraphic.getHolder();
            else
                sh = null;

            interval_counter = 0;
        }

        @Override
        public void run() {
            Canvas c;
            while(!Thread.interrupted() && sh != null) {
                if(interval_counter < INTERVALS_UNTIL_WAIT ||
                        interval_counter >= INTERVALS_UNTIL_WAIT + MAX_WAIT_INTERVALS) {
                    synchronized (sh) {
                        c = sh.lockCanvas();
                        if (c != null) {
                            try {
                                tick(c);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                sh.unlockCanvasAndPost(c);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(INTERVAL);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }

                interval_counter++;
            }
        }
    }

    private class Bottle {
        private float percentageComplete;
        private Bitmap empty_bottle;

        private int mDrainVelocity;

        Bottle() {
            importBitmap();
            percentageComplete = 0;
            mDrainVelocity = 0;
        }

        void setDrainVelocity(int d_vel) {
            mDrainVelocity = d_vel;
        }

        void setPercentageComplete(int percentage) {
            percentageComplete = 100 - percentage;
        }

        private void importBitmap() {
            empty_bottle = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.bottle, new BitmapFactory.Options());
        }

        private void updatePercentage() {
            if(percentageComplete < 100)
                percentageComplete += mDrainVelocity;
        }


        void restart() {
            percentageComplete = 100;
        }





        private void clearCanvas(Canvas c) {
            Paint brush = new Paint();
            brush.setColor(Color.BLACK);
            Rect surface = new Rect(0, 0, getWidth(), getHeight());
            c.drawRect(surface, brush);
        }

        private void paintLevel(Canvas c) {
            int percentage = (int)(getHeight() * percentageComplete / 100);

            Paint brush = new Paint();
            brush.setColor(Color.CYAN);
            Rect surface = new Rect(0, percentage, getWidth(), getHeight());
            c.drawRect(surface, brush);
        }

        private void paintBottle(Canvas c) {
            Paint brush = new Paint();
            Rect surface = new Rect(0, 0, getWidth(), getHeight());
            c.drawBitmap(empty_bottle, null, surface, brush);
        }

        void update(Canvas c) {
            clearCanvas(c);
            updatePercentage();

            paintLevel(c);
            paintBottle(c);
        }
    }


    BottleThread bottleThread;
    Bottle bottle;

    public BottleGraphic(Context context) {
        super(context);
        initialize();
    }

    public BottleGraphic(Context context, AttributeSet attrs) {
        super(context,attrs);
        initialize();
    }

    public BottleGraphic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        bottle = new Bottle();
        bottleThread = new BottleThread(BottleGraphic.this);
        SurfaceHolder sh = getHolder();
        sh.addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder sh) {
        Log.v("BottleSurfaceView", "Surface being created");

        if(
                bottleThread != null &&
                (!bottleThread.isAlive() || bottleThread.isInterrupted())
                )
            bottleThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder sh, int width, int height, int format) {
        Log.v("BottleSurfaceView", "Surface being changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder sh) {
        Log.v("BottleSurfaceView", "Surface being destroyed");
        if(bottleThread != null && !bottleThread.isInterrupted())
            bottleThread.interrupt();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            bottle.restart();
            return true;
        }
        return false;
    }

    public void tick(Canvas c) {
        bottle.update(c);
    }



    public void setBottleDVel(int d_vel) {
        bottle.setDrainVelocity(d_vel);
    }

    public void setPercentageComplete(int percentage) {
        bottle.setPercentageComplete(percentage);
    }
}
