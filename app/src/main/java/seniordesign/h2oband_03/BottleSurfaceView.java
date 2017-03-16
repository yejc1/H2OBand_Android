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

public class BottleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private class BottleThread extends Thread implements Runnable {
        final int INTERVAL = 100;

        private final SurfaceHolder sh;
        private BottleSurfaceView bottleSurfaceView;

        BottleThread(BottleSurfaceView bottleSurfaceView) {
            this.bottleSurfaceView = bottleSurfaceView;
            if(bottleSurfaceView != null)
                sh = bottleSurfaceView.getHolder();
            else
                sh = null;
        }

        @Override
        public void run() {
            Canvas c;
            while(!Thread.interrupted() && sh != null) {
                synchronized(sh) {
                    c = sh.lockCanvas();
                    if(c != null) {
                        try {
                            tick(c);
                        } catch(Exception e) {
                            e.printStackTrace();
                        } finally {
                            sh.unlockCanvasAndPost(c);
                        }
                    }
                }

                try {
                    Thread.sleep(INTERVAL);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Bottle {
        private int percentageComplete;
        private Bitmap empty_bottle;

        public Bottle() {
            importBitmap();
            percentageComplete = 0;
        }

        private void importBitmap() {
            empty_bottle = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.bottle, new BitmapFactory.Options());
        }

        private void updatePercentage() {
            if(percentageComplete < 100)
                percentageComplete += 1;
        }


        void restart() {
            percentageComplete = 0;
        }

        private void clearCanvas(Canvas c) {
            Paint brush = new Paint();
            brush.setColor(Color.BLACK);
            Rect surface = new Rect(0, 0, getWidth(), getHeight());
            c.drawRect(surface, brush);
        }

        private void paintLevel(Canvas c) {
            int percentage = getHeight() * percentageComplete / 100;

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

    public BottleSurfaceView(Context context) {
        super(context);
        initialize();
    }

    public BottleSurfaceView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initialize();
    }

    public BottleSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        bottle = new Bottle();
        bottleThread = new BottleThread(BottleSurfaceView.this);
        SurfaceHolder sh = getHolder();
        sh.addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder sh) {
        if(bottleThread != null)
            bottleThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder sh, int width, int height, int format) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder sh) {
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
}
